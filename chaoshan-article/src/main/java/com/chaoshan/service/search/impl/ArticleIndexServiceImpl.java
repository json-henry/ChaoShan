package com.chaoshan.service.search.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.Article;
import com.chaoshan.entity.ArticleCollection;
import com.chaoshan.entity.ArticleStar;
import com.chaoshan.entity.UserArticle;
import com.chaoshan.entity.dto.ArticleSearchDTO;
import com.chaoshan.mapper.*;
import com.chaoshan.service.ArticleTagService;
import com.chaoshan.service.search.ArticleIndexService;
import com.chaoshan.service.search.RedisService;
import com.chaoshan.util.UtilMethod;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.stream.Collectors;

import static com.chaoshan.common.constant.ArticleConstant.ARTICLE_MAPPING;
import static com.chaoshan.common.constant.MqConstants.ARTICLE_EXCHANGE;
import static com.chaoshan.common.constant.MqConstants.ARTICLE_HOT_SEARCH_ROUTING;

/**
 * @DATE: 2022/05/15 21:46
 * @Author: 小爽帅到拖网速
 */
@Service
@Slf4j
public class ArticleIndexServiceImpl implements ArticleIndexService {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleStarMapper articleStarMapper;
    @Autowired
    private ArticleCollectionMapper articleCollectionMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private UserArticleMapper userArticleMapper;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisService redisService;

    /**
     * 创建article索引
     */
    @Override
    @SneakyThrows
    public boolean createArticleIndex() {
        // 判断索引是否存在
        GetIndexRequest request = new GetIndexRequest("article");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        if (!exists) {
            CreateIndexRequest createquest = new CreateIndexRequest("article");
            createquest.mapping(ARTICLE_MAPPING, XContentType.JSON);
            if (client.indices().create(createquest, RequestOptions.DEFAULT).isAcknowledged()) {
                log.info("创建索引库成功！");
                // 填充数据
                fillBatchArticleData(null);
            }
            return !exists;
        }
        return exists;
    }


    /**
     * 填充数据索引
     */
    @Override
    @SneakyThrows
    public R fillBatchArticleData(String ids) {
        List<Article> articleListDb = null;
        if (ids != null) {
            List<String> idsList = Arrays.asList(ids.split(","));
            articleListDb = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                    .eq(Article::getIsExamine, true).eq(Article::getIsPublish, true).in(Article::getId, idsList));
        } else {
            articleListDb = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                    .eq(Article::getIsExamine, true).eq(Article::getIsPublish, true));
        }
        if (ObjectUtil.isEmpty(articleListDb.size() == 0)) {
            return R.fail("ids出现异常");
        }
        // 处理数据，填充点赞数收藏数，图片路径分割，转换为ArticleSearchDTO数据类型
        List<ArticleSearchDTO> handledResult = handleSearchData(articleListDb);
        if (CollectionUtil.isEmpty(handledResult)) {
            return R.fail("插入数据失败");
        }
        // 批量导入文档
        BulkRequest bulkRequest = new BulkRequest();
        for (ArticleSearchDTO data : handledResult) {
            bulkRequest.add(new IndexRequest("article").id(data.getId().toString())
                    .source(JSONUtil.toJsonStr(data), XContentType.JSON));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return R.success(ResultCode.SUCCESS);
    }

    /**
     * 填充后台审核通过的多条数据
     *
     * @return
     */
    @Override
    @SneakyThrows
    public R fillArticleData(String accountids) {
        return fillBatchArticleData(accountids);
    }

    /**
     * 删除单条数据
     *
     * @param accoutid
     * @return
     */
    @Override
    @SneakyThrows
    public R deleteArticleData(String accoutid) {
        DeleteRequest deleteRequest = new DeleteRequest("article", accoutid.toString());
        int successful = client.delete(deleteRequest, RequestOptions.DEFAULT).getShardInfo().getSuccessful();
        return successful == 1 ? R.success(ResultCode.SUCCESS) : R.fail(ResultCode.FAILURE);
    }

    /**
     * 更新单条数据
     *
     * @param accountid
     * @return
     */
    @Override
    public R updateArticleData(String accountid) {
        if (deleteArticleData(accountid).isSuccess()) {
            return fillArticleData(accountid);
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 根据关键字以及类型进行搜索,并将搜素结果中的关键字高亮返回
     *
     * @param keyword
     * @param type
     * @return
     */
    @Override
    @SneakyThrows
    @Retryable(value = SocketTimeoutException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 2))
    public R<Map<String, Object>> articleSearch(String keyword, Integer type) {

        SearchRequest searchRequest = new SearchRequest("article");
        if (ObjectUtil.isEmpty(keyword) && ObjectUtil.isEmpty(type)) {
            // 构造搜索条件
            searchRequest.source().query(QueryBuilders.matchAllQuery())
                    .sort("sum", SortOrder.DESC).size(50);
        } else if (ObjectUtil.isNotEmpty(keyword) && ObjectUtil.isEmpty(type)) {
            // 构造搜索条件
            searchRequest.source()
                    .query(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery("all", keyword)))
                    .highlighter(new HighlightBuilder()
                            .field("details").requireFieldMatch(false)
                            .field("introduction").requireFieldMatch(false)
                            .field("title").preTags(" <span style=\"color:#f00\">").postTags("</span>").requireFieldMatch(false))
                    .sort("sum", SortOrder.DESC).size(50);
        } else {
            // 构造搜索条件
            searchRequest.source()
                    .query(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery("all", keyword))
                            .filter(QueryBuilders.termQuery("type", type)))
                    .highlighter(new HighlightBuilder()
                            .field("details").requireFieldMatch(false)
                            .field("introduction").requireFieldMatch(false)
                            .field("title").preTags(" <span style=\"color:#f00\">").postTags("</span>").requireFieldMatch(false))
                    .sort("sum", SortOrder.DESC).size(50);
        }
        // 发送请求
        SearchHits searchHits = client.search(searchRequest, RequestOptions.DEFAULT).getHits();
        long value = 0L;
        if ((ObjectUtil.isNotEmpty(searchHits.getTotalHits())) && (value = searchHits.getTotalHits().value) < 0) {
            return R.fail(ResultCode.FAILURE, "未搜索到匹配内容");
        }

        log.info("总共获取的条数：{}", value);

        // 处理索引库匹配结果
        List<ArticleSearchDTO> handleResult = handleIArticleSearch(keyword, searchHits);
        if (handleResult.size() > 0 && ObjectUtil.isNotEmpty(keyword)) {
            // 添加热搜词
            rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_HOT_SEARCH_ROUTING, keyword);

        }
        Map<String, Object> map = new HashMap<>();
        map.put("size", value);
        map.put("data", handleResult);
        return R.data(map);
    }

    /**
     * 相关查询返回
     *
     * @param keyword
     * @return
     */
    @Override
    @SneakyThrows
    public List<ArticleSearchDTO> articleRealted(String keyword) {
        SearchRequest searchRequest = new SearchRequest("article");

        // 构造搜索条件
        searchRequest.source()
                .query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("all", keyword)))
                .sort("sum", SortOrder.DESC).size(50);
        // 发送请求
        SearchHits searchHits = client.search(searchRequest, RequestOptions.DEFAULT).getHits();
        long value = 0L;
        if ((ObjectUtil.isNotEmpty(searchHits.getTotalHits())) && (value = searchHits.getTotalHits().value) < 0) {
            return null;
        }
        log.info("总共获取的条数：{}", value);

        // 处理索引库匹配结果
        List<ArticleSearchDTO> handleResult = handleIArticleSearch(keyword, searchHits);

        return handleResult;
    }


    /**
     * 返回综合模块中的前8篇推荐文章
     *
     * @return
     */
    @Override
    @SneakyThrows
    public List<ArticleSearchDTO> recommendationDaily() {
        SearchRequest searchRequest = new SearchRequest("article");
        searchRequest.source().sort("sum", SortOrder.DESC).size(8);
        SearchHit[] hits = client.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits();
        List<ArticleSearchDTO> searchData = Arrays.stream(hits).map(hit -> {
            return JSONUtil.toBean(hit.getSourceAsString(), ArticleSearchDTO.class);
        }).collect(Collectors.toList());
        return searchData;
    }

    /**
     * 热搜
     *
     * @return
     */
    @Override
    public R<List<Map<String, Object>>> articleHotSearch() {
        List<Map<String, Object>> hotList = redisService.getHotList(null);
        return R.data(hotList);
    }

    /**
     * 定时更新索引库中的浏览量，点赞数，收藏数
     */
    @Override
    @SneakyThrows
    @Scheduled(cron = "0 0 0/6 * * ? ")
    public void taskUpdateArticleIndex() {
        // 查询索引库中所有文章id
        List<Long> idsByArticleIndex = getIdsByArticleIndex();
        if (CollectionUtils.isEmpty(idsByArticleIndex)) {
            return;
        }
        // 根据id查询文章的点赞数，收藏数，浏览量，并更新sum
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getPageView).in(Article::getId, idsByArticleIndex));
        // 处理文章数据，填充点赞数，收藏数并转换DTO返回
        List<ArticleSearchDTO> articleSearchDTOS = updateArticleSumData(articleList);
        for (ArticleSearchDTO articleSearchDTO : articleSearchDTOS) {
            UpdateRequest updateRequest = new UpdateRequest("article", articleSearchDTO.getId().toString());
            updateRequest.doc("sum", articleSearchDTO.getSum(), "pageView", articleSearchDTO.getPageView(),
                    "starCount", articleSearchDTO.getStarCount(), "collectionCount",
                    articleSearchDTO.getCollectionCount());
            client.update(updateRequest, RequestOptions.DEFAULT);
        }
    }

    /**
     * 填充热搜值
     */
    @Override
    public void fillHotSearchValue() {

    }

    /**
     * 获取轮播图
     *
     * @return
     */
    @Override
    public List<ArticleSearchDTO> shufflingfigure() throws IOException {

        SearchRequest searchRequest = new SearchRequest("article");
        searchRequest.source().sort("createTime", SortOrder.DESC).size(4);
        SearchHit[] hits = client.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits();
        List<ArticleSearchDTO> searchData = Arrays.stream(hits).map(hit -> {
            return JSONUtil.toBean(hit.getSourceAsString(), ArticleSearchDTO.class);
        }).collect(Collectors.toList());
        return searchData;
    }


    // 处理文章数据，填充点赞数，收藏数并转换DTO返回
    private List<ArticleSearchDTO> updateArticleSumData(List<Article> articleList) {
        List<ArticleSearchDTO> handleResult = articleList.stream().map(article -> {
            // 填充点赞数
            article.setStarCount(getArticleStarCount(article));
            // 填充收藏数
            article.setCollectionCount(getArticleCollectionCount(article));
            ArticleSearchDTO articleSearchDTO = new ArticleSearchDTO();
            BeanUtil.copyProperties(article, articleSearchDTO);
            // 填充sum
            articleSearchDTO.setSum(getSum(articleSearchDTO));
            return articleSearchDTO;
        }).collect(Collectors.toList());
        return handleResult;
    }

    // 获取索引库中现存所有文章的id
    private List<Long> getIdsByArticleIndex() throws IOException {
        SearchRequest searchRequest = new SearchRequest("article");
        searchRequest.source()
                .aggregation(AggregationBuilders.terms("ids")
                        .field("id").size(100));
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms terms = (Terms) searchResponse.getAggregations().get("ids");
        List<Long> idsByArticleIndex = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        if (buckets.size() == 0) {
            return null;
        }
        for (Terms.Bucket bucket : buckets) {
            idsByArticleIndex.add(Long.valueOf(bucket.getKeyAsString()));
        }
        return idsByArticleIndex;
    }


    /**
     * 处理标签1,2,3 返回#xxx#xxx
     *
     * @param article
     * @return
     */
    private String handleTagIdReturnTagName(Article article) {
        if (ObjectUtil.isEmpty(article.getTag())) {
            return null;
        }
        Object[] tagNameArray = articleTagService.getTagsByIds(article.getTag());
        StringBuffer stringBuffer = new StringBuffer();
        for (Object o : tagNameArray) {
            stringBuffer.append("#" + o.toString());
        }
        return stringBuffer.toString();
    }

    // 处理索引库匹配结果
    private List<ArticleSearchDTO> handleIArticleSearch(String keyword, SearchHits searchHits) {
        SearchHit[] hits = searchHits.getHits();
        return Arrays.stream(hits).map(hit -> {
            String highlightDetails = null;
            String highlightIntroduction = null;
            String highlightTitle = null;
            if ((!CollectionUtils.isEmpty(hit.getHighlightFields()))) {
                // 获取高亮字段

                HighlightField details = hit.getHighlightFields().get("details");
                if (ObjectUtil.isNotEmpty(details)) {
                    highlightDetails = details.getFragments()[0].string();
                }
                HighlightField introduction = hit.getHighlightFields().get("introduction");
                if (ObjectUtil.isNotEmpty(introduction)) {
                    highlightIntroduction = introduction.getFragments()[0].string();
                }
                HighlightField title = hit.getHighlightFields().get("title");
                if (ObjectUtil.isNotEmpty(title)) {
                    highlightTitle = title.getFragments()[0].string();
                }
            }
            ArticleSearchDTO articleSearchDTO = JSONUtil.toBean(hit.getSourceAsString().toString(),
                    ArticleSearchDTO.class);
            if (ObjectUtil.isEmpty(keyword)) {
                return articleSearchDTO;
            }
            // 替换高亮文本关键字
            return handleHighlightReplace(keyword, highlightDetails, highlightIntroduction, highlightTitle,
                    articleSearchDTO);

        }).collect(Collectors.toList());
    }

    // 处理高亮文本关键字
    private ArticleSearchDTO handleHighlightReplace(String keyword, String highlightDetails,
                                                    String highlightIntroduction, String highlightTitle,
                                                    ArticleSearchDTO articleSearchDTO) {
        // 替换details中的关键字
        String details = articleSearchDTO.getDetails();
        if (!ObjectUtil.hasEmpty(highlightDetails, details)) {
            // articleSearchDTO.setDetails(details.replace(keyword, highlightDetails));
            articleSearchDTO.setDetails(highlightDetails);
        }
        // 替换简介中的关键字
        String introduction = articleSearchDTO.getIntroduction();
        if (!ObjectUtil.hasEmpty(highlightIntroduction, introduction)) {
            // articleSearchDTO.setIntroduction(introduction.replace(keyword, highlightIntroduction));
            articleSearchDTO.setIntroduction(highlightIntroduction);
        }
        // // 替换标题中的关键字
        String title = articleSearchDTO.getTitle();
        if (!ObjectUtil.hasEmpty(highlightTitle, title)) {
            // String replace = title.replace(keyword, highlightTitle);
            // articleSearchDTO.setTitle(replace);
            articleSearchDTO.setTitle(highlightTitle);
        }

        return articleSearchDTO;
    }

    /**
     * 处理数据，填充点赞数收藏数，图片路径分割，转换为ArticleSearchDTO数据类型
     *
     * @param articleList
     * @return
     */
    private List<ArticleSearchDTO> handleSearchData(List<Article> articleList) {
        return articleList.stream().map(article -> {
            // 获取文章点赞数
            article.setStarCount(getArticleStarCount(article));
            // 获取文章收藏数
            article.setCollectionCount(getArticleCollectionCount(article));
            if (ObjectUtil.isNotEmpty(article.getDetails()) && article.getDetails().length() > 50) {
                article.setDetails(article.getDetails().substring(0, 50) + "...");
            }
            // 获取文章作者账号
            UserArticle userArticle =
                    userArticleMapper.selectOne(new LambdaQueryWrapper<UserArticle>().eq(UserArticle::getArticleid,
                            article.getId()));
            if (ObjectUtil.isEmpty(userArticle)) {
                return null;
            }
            String accountid = userArticle.getAccountid();
            // 处理tag
            article.setTag(handleTagIdReturnTagName(article));

            ArticleSearchDTO articleSearchDTO = new ArticleSearchDTO();
            // 实体复制 处理图片路径字段分割
            BeanUtil.copyProperties(divisionPictureLink(article), articleSearchDTO);

            // 填充点赞数+收藏数+浏览量
            articleSearchDTO.setSum(getSum(articleSearchDTO));
            articleSearchDTO.setAccountid(accountid);
            return articleSearchDTO;
        }).collect(Collectors.toList());
    }

    /**
     * 返回点赞数+收藏数+浏览量
     *
     * @param articleSearchDTO
     * @return
     */
    private int getSum(ArticleSearchDTO articleSearchDTO) {
        int sum = 0;
        if (ObjectUtil.isNotEmpty(articleSearchDTO.getStarCount())) {
            sum += articleSearchDTO.getStarCount();
        }
        if (ObjectUtil.isNotEmpty(articleSearchDTO.getCollectionCount())) {
            sum += articleSearchDTO.getCollectionCount();
        }
        if (ObjectUtil.isNotEmpty(articleSearchDTO.getPageView())) {
            sum += articleSearchDTO.getPageView();
        }
        return sum;
    }

    /**
     * 处理图片路径字段分割
     *
     * @param article
     * @return
     */
    private Article divisionPictureLink(Article article) {
        // 分割图片链接
        List<String> linkList = UtilMethod.divisionLinkString(article.getPictureLink(), ";");
        if (!(linkList.size() > 0)) {
            return article;
        }
        article.setPictureLink(linkList.get(0));
        return article;
    }

    /**
     * 获取文章点赞数
     *
     * @param article
     * @return
     */
    private Integer getArticleStarCount(Article article) {
        return articleStarMapper.selectCount(
                new LambdaQueryWrapper<ArticleStar>().eq(ArticleStar::getArticleid, article.getId())
        );
    }

    /**
     * 获取文章收藏数
     *
     * @param article
     * @return
     */
    private Integer getArticleCollectionCount(Article article) {
        return articleCollectionMapper.selectCount(
                new LambdaQueryWrapper<ArticleCollection>().eq(ArticleCollection::getArticleid, article.getId()));
    }
}
