package com.chaoshan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.UserClient;
import com.chaoshan.entity.*;
import com.chaoshan.entity.dto.ArticleDto;
import com.chaoshan.entity.dto.ArticleSearchDTO;
import com.chaoshan.entity.dto.ArticleTypeDTO;
import com.chaoshan.entity.dto.ArticleTypeListDTO;
import com.chaoshan.mapper.ArticleCollectionMapper;
import com.chaoshan.mapper.ArticleMapper;
import com.chaoshan.mapper.ArticleStarMapper;
import com.chaoshan.mapper.UserArticleMapper;
import com.chaoshan.service.*;
import com.chaoshan.service.search.ArticleIndexService;
import com.chaoshan.util.IpUtils;
import com.chaoshan.util.UtilMethod;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import com.chaoshan.util.api.Rpage;
import com.chaoshan.util.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.chaoshan.common.constant.ArticleConstant.*;
import static com.chaoshan.common.constant.ArticleIndexConstant.*;
import static com.chaoshan.common.constant.MqConstants.*;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleCollectionMapper articleCollectionMapper;
    @Autowired
    private ArticleStarMapper articleStarMapper;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ArticleCommentService articleCommentService;
    @Autowired
    private UserArticleService userArticleService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private UserArticleMapper userArticleMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ArticleIndexService articleIndexService;
    @Autowired
    private ArticleReplyService articleReplyService;
    @Autowired
    private ArticleCommentReplyStarService articleCommentReplyStarService;
    @Autowired
    private ArticleCollectionService articleCollectionService;
    @Autowired
    private AliyunOssServiceImpl aliyunOssService;

    /**
     * 分页获取文章数据
     *
     * @param currentPage 当前页
     * @param size        记录数
     * @param type        类型
     * @return
     */
    @Override
    @Transactional
    public R<Rpage> getArtPage(Integer currentPage, Integer size, Integer type) {
        Page<Article> articlePage = new Page<>(currentPage, size);
        IPage<Article> articleIPage = articleMapper.getArtPage(articlePage, type);
        List<ArticleDto> articleDtos = articleIPage.getRecords().stream().map(article -> {
            // 获取文章点赞数
            Integer collectionCount = getArticleCollectionCount(article);
            // 获取文章收藏数
            Integer starCount = getArticleStarCount(article);
            ArticleDto articleDto = BeanUtil.copyProperties(article, ArticleDto.class);
            articleDto.setCollectionCount(collectionCount);
            articleDto.setStarCount(starCount);
            return articleDto;
        }).collect(Collectors.toList());
        return R.data(new Rpage(articleIPage.getTotal(), articleDtos));
    }

    /**
     * 每日推荐
     *
     * @return
     */
    @Override
    public R<List<ArticleSearchDTO>> recommendationDaily() {
   /* List<Article> articleList = articleMapper.recommendationDaily();
    List<Article> articleHandleResult = articleList.stream().map(article -> {
      // 获取文章收藏数 点赞数 浏览量
      article.setCountMerge((int) (getCountMerge(article)));
      // 处理图片字段分割
      return divisionPictureLink(article);
    }).sorted(Comparator.comparingInt(Article::getCountMerge).reversed()).limit(8).collect(Collectors.toList());*/
        List<ArticleSearchDTO> articleHandleResult = articleIndexService.recommendationDaily();
        return R.data(articleHandleResult);
    }

    /**
     * 根据文章id返回文章详情
     *
     * @param id
     * @return
     */
    @Override
    public R<Article> getArticleById(Integer id, HttpServletRequest request) {
        Article article = articleMapper.getArticleById(id);
        if (ObjectUtil.isEmpty(article)) {
            return R.fail(ResultCode.FAILURE, "该id的文章不存在");
        }
        // 获取文章评论,评论的点赞以及评论的回复和评论回复的点赞
        List<ArticleComment> articleCommentList = articleCommentService.getCommentByArticleId(article.getId());
        article.setArticleComment(articleCommentList);
        // 获取文章用户
        // 判断文章所属者是否是管理员，如果是则不返回文章用户信息
        if (!ObjectUtil.hasEmpty(article.getIsManager()) && !article.getIsManager()) {
            User articleUser = userArticleService.getUserByArticleId(article.getId());
            article.setUser(articleUser);
        }
        // 处理文章标签
        article.setTag(handleTagIdReturnTagName(article));
        // 图片链接切割
        article.setPictureLinks(UtilMethod.divisionLinkString(article.getPictureLink(), ";"));
        // 异步处理文章浏览量自增
        handleArticlePageViewIncrement(request, article);

        // 填充文章相关数据
        List<ArticleSearchDTO> articleRealted = articleIndexService.articleRealted(article.getTitle());
        if (CollectionUtil.isEmpty(articleRealted)) {
            return R.data(article);
        }
        List<ArticleSearchDTO> handleResult =
                articleRealted.stream().filter(a -> a.getId() != article.getId()).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        map.put("size", handleResult.size());
        map.put("data", handleResult);
        article.setRelatedArticles(map);

        return R.data(article);
    }

    /**
     * 异步处理文章浏览量自增
     *
     * @param request
     * @param article
     */
    private void handleArticlePageViewIncrement(HttpServletRequest request, Article article) {
        // 文章浏览量+1 去除重复ip
        HashMap<String, Object> map = new HashMap<>();
        String ipAddr = IpUtils.getIpAddr(request);
        map.put("ip", ipAddr);
        map.put("articleid", article.getId());

        rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_PAGEVIEW_ROUTING, JSONUtil.toJsonStr(map));
    }

    /**
     * 处理保存在文章数据里边tag存储的tag表的主键，查询tagname并返回
     *
     * @param article
     * @return
     */
    private String handleTagIdReturnTagName(Article article) {
        Object[] tagNameArray = articleTagService.getTagsByIds(article.getTag());
        StringBuffer stringBuffer = new StringBuffer();
        for (Object o : tagNameArray) {
            stringBuffer.append("#" + o.toString());
        }
        return stringBuffer.toString();
    }

    /**
     * 根据类型返回文章列表,类型：1(饮食文化) 2(名人景点) 3(民间艺术) 4(潮玩攻略)
     *
     * @param type
     * @return
     */
    @Override
    public R<ArticleTypeListDTO> getArticleListByType(Integer type, Integer count) {

        List<Article> articleList = null;

        // 判断是否是饮食文化模块，饮食文化模块需要返回商家推荐产品
        if (type == DIET_CULTURE_MODULE) {
            articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>().
                    eq(Article::getType, type).eq(Article::getIsExamine, true)
                    .eq(Article::getIsPublish, true).eq(Article::getIsProduct, true));
        } else {
            // 根据类别获取通过审核，已发布的文章列表
            articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>().
                    eq(Article::getType, type).eq(Article::getIsExamine, true).eq(Article::getIsPublish, true));
        }
        // 根据收藏数、填充点赞数、浏览量来挑选符合的前count条
        List<Article> handleResult = articleList.stream().map(article -> {
            // 获取文章收藏数
            Integer collectionCount = getArticleCollectionCount(article);
            // 填充收藏数
            article.setCollectionCount(collectionCount);
            // 获取文章点赞数
            Integer starCount = getArticleStarCount(article);
            // 填充点赞数
            article.setStarCount(starCount);
            int countMerge = getCountMerge(article);
            article.setCountMerge(countMerge);
            // 文章正文内容大于50个字，则截取正文内容的前50个字并添加...
            String a = null;
            if (StringUtils.hasText(a = article.getIntroduction()) && a.length() > 50) {
                article.setDetails(a.substring(0, 50) + "...");
            }
            // 处理图片link分割
            return divisionPictureLink(article);
            // 根据综合值降序返回
        }).sorted(Comparator.comparingInt(Article::getCountMerge).reversed()).limit(count).collect(Collectors.toList());

        // 处理返回的articleList
        List<ArticleTypeDTO> articleTypeDTOS = handleResult.stream().
                map(UtilMethod::copyArticleIgnoreField).collect(Collectors.toList());

        ArticleTypeListDTO articleTypeListDTO = new ArticleTypeListDTO();
        articleTypeListDTO.setArticles(articleTypeDTOS);

        // 判断type是否是饮食文化还是潮流攻略，如果不是直接返回处理结果
        if (type == DIET_CULTURE_MODULE) {
            // 饮食文化，需要额外返回热门
            LocalDateTime now = LocalDateTime.now();
            List<Article> dietArticles = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                    .eq(Article::getType, type)
                    .between(Article::getCreateTime, now.plusMonths(-2), now.plusMonths(2))
                    .orderByDesc(Article::getPageView).last("limit " + count));
            // 筛选处理字段
            List<ArticleTypeDTO> dietHandleResult = handleArticleField(dietArticles);

            Map<String, List<ArticleTypeDTO>> map = new HashMap<>();
            map.put("hot", dietHandleResult);
            articleTypeListDTO.setMap(map);
        }
        if (type == PLAY_STRATEGY_MODULE) {
            // 潮玩攻略 需要返回当季，室内室外景区推荐文章
            int currentMonthValue = LocalDateTime.now().getMonthValue();
            // 获取室内外景区,过滤最近3个月外的数据，按照浏览量降序
            // 先获取室内景区
            List<Article> indoorArticlesDB = getArticlesByDoor(FAMOUS_SCENIC_MODULE, count, currentMonthValue, "室内");
            // 获取室外景区
            List<Article> outdoorArticlesDB = getArticlesByDoor(FAMOUS_SCENIC_MODULE, count, currentMonthValue, "室外");

            Map<String, List<ArticleTypeDTO>> map = new HashMap<>();

            // 处理室内
            List<ArticleTypeDTO> inDoorScenic = handleArticleField(indoorArticlesDB);

            // 处理室外
            List<ArticleTypeDTO> outDoorScenic = handleArticleField(outdoorArticlesDB);

            map.put("indoor", inDoorScenic);
            map.put("outdoor", outDoorScenic);
            articleTypeListDTO.setMap(map);

        }

        return R.data(articleTypeListDTO);
    }


    /**
     * 用户查询已发布的文章,包括封面，标题，点赞数，收藏数，时间
     *
     * @param accountid
     * @return
     */
    @Override
    public R<List<Article>> userPubilshedArticle(String accountid, String keyword) {
        // 获取文章基础数据
        List<Article> articlesDb = articleMapper.userPubilshedArticle(accountid, keyword);
        // 处理文章数据
        List<Article> articlesHandleResult = getArticlesHandleResult(articlesDb);
        return R.data(articlesHandleResult);
    }


    /**
     * 用户查询未发布的文章,包括封面，标题，点赞数，收藏数，时间
     *
     * @param accountid
     * @return
     */
    @Override
    public R<List<Article>> getDrafts(String accountid, String keyword) {
        // 处理文章基础数据
        List<Article> articlesDb = articleMapper.getDrafts(accountid, keyword);
        // 处理文章数据
        List<Article> articlesHandleResult = getArticlesHandleResult(articlesDb);
        return R.data(articlesHandleResult);
    }

    /**
     * 用户收藏的文章列表
     *
     * @param accountid
     * @return
     */
    @Override
    public R<List<Article>> getUserCollectionArticles(String accountid, String keyword) {
        // 查询收藏表里的所有文章id
        List<ArticleCollection> articleCollections =
                articleCollectionMapper.selectList(new LambdaQueryWrapper<ArticleCollection>()
                .eq(ArticleCollection::getAccountid, accountid).select(ArticleCollection::getArticleid));
        if (articleCollections.size() == 0) {
            return null;
        }
        // 提取id列表
        List<Long> ids = articleCollections.stream().map(articleCollection -> {
            return articleCollection.getArticleid();
        }).collect(Collectors.toList());
        List<Article> articlesHandleResult = handleArticlesByIds(ids, keyword);

        return R.data(articlesHandleResult);

    }


    /**
     * 用户点赞文章列表
     *
     * @param accountid
     * @return
     */
    @Override
    public R<List<Article>> userStarArticles(String accountid, String keyword) {
        // 根据账号查询点赞表里所有文章id
        List<ArticleStar> articleStars = articleStarMapper.selectList(new LambdaQueryWrapper<ArticleStar>()
                .eq(ArticleStar::getAccountid, accountid).select(ArticleStar::getArticleid));
        // 提取id
        List<Long> ids =
                articleStars.stream().map(articleStar -> articleStar.getArticleid()).collect(Collectors.toList());
        // 根据id查询
        return R.data(handleArticlesByIds(ids, keyword));
    }

    /**
     * 商家发布产品
     *
     * @param pictures
     * @param article
     * @param tags
     * @return
     */
    @Override
    public R postProduct(String[] pictures, Article article, String[] tags, String accountid) {

        Article handledArticle = handleFieldPostOper(pictures, article, tags, true);
        if (ObjectUtil.isNotEmpty(handledArticle) && ObjectUtil.isNotEmpty(handledArticle.getId())) {
            if (articleMapper.updateById(handledArticle) > 0) {
                return R.success(ResultCode.SUCCESS);
            } else {
                return R.fail(ResultCode.FAILURE);
            }
        }
        if (articleMapper.insert(handledArticle) > 0 && userArticleMapper.insert(new UserArticle().setArticleid(handledArticle.getId())
                // .setAccountid( LoginUser.getCurrentLoginUser().getAccountid());
                .setAccountid(accountid)) > 0) {
            return R.success(ResultCode.SUCCESS);
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 用户发布产品
     *
     * @param pictures
     * @param article
     * @param tags
     * @return
     */
    @Override
    public R postArticle(String[] pictures, Article article, String[] tags, String acccountid) {
        Article handledArticle = handleFieldPostOper(pictures, article, tags, false);
        String accountid = LoginUser.getCurrentLoginUser().getAccountid();
        if (ObjectUtil.isNotEmpty(handledArticle) && ObjectUtil.isNotEmpty(handledArticle.getId())) {
            if (articleMapper.updateById(handledArticle) > 0) {
                return R.success(ResultCode.SUCCESS);
            } else {
                return R.fail(ResultCode.FAILURE);
            }
        }
        if (articleMapper.insert(handledArticle) > 0 && userArticleMapper.insert(new UserArticle().setArticleid(handledArticle.getId())
                .setAccountid(acccountid)) > 0) {
            return R.success(ResultCode.SUCCESS);
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 待审核文章列表
     *
     * @param accountid
     * @return
     */
    @Override
    public R<List<Article>> waitPublish(String accountid, String keyword) {
        // 查询用户文章表中用户所有的文章id
        List<Long> ids = userArticleMapper.selectList(new LambdaQueryWrapper<UserArticle>()
                        .eq(UserArticle::getAccountid, accountid).select(UserArticle::getArticleid))
                .stream().map(userArticle -> userArticle.getArticleid()).collect(Collectors.toList());

        // 根据ids查询用户未发布的文字，返回字段有文章id，文章图片链接，文章内容
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getPictureLink, Article::getDetails, Article::getTitle)
                .like(ObjectUtil.isNotEmpty(keyword), Article::getTitle, keyword)
                .eq(Article::getIsExamine, false).eq(Article::getIsPublish, true).in(ids.size() > 0, Article::getId,
                        ids));
        List<Article> handledResult = articleList.stream().map(article -> {
            // 处理图片列表
            List<String> pictureLinks = UtilMethod.divisionLinkString(article.getPictureLink(), ";");
            if (!(CollectionUtils.isEmpty(pictureLinks))) {
                article.setPictureLink(pictureLinks.get(0));
            }
            // 处理文章字数
            if (ObjectUtil.isNotEmpty(article.getDetails()) && article.getDetails().length() > 50) {
                article.setDetails(article.getDetails().substring(0, 50) + "...");
            }
            return article;
        }).collect(Collectors.toList());
        return R.data(handledResult);
    }

    /**
     * 根据文章id修改文章审核状态
     *
     * @param accountids
     * @return
     */
    @Override
    public R examineArticle(String[] accountids) {
        List<String> accountidsList = Arrays.asList(accountids);
        int count = articleMapper.update(new Article().setIsExamine(true),
                new LambdaQueryWrapper<Article>().in(Article::getId, accountidsList));
        if (accountids.length == count) {
            rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_INSERT_ROUTING, String.join(",", accountidsList));
            return R.success(ResultCode.SUCCESS);
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 删除文章
     *
     * @param accountid
     * @param articleid
     * @return
     */
    @Override
    @Transactional
    public R deleteArticle(Integer articleid) {
        User currentLoginUser = LoginUser.getCurrentLoginUser();
        if (ObjectUtil.isEmpty(currentLoginUser)) {
            return R.fail(ResultCode.FAILURE, "请先登录！");
        }
        String accountid = currentLoginUser.getAccountid();
        // 查询文章是否存在
        Article articleDb = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getId,
                Article::getPictureLink));

        if (ObjectUtil.isEmpty(articleDb)) {
            return R.fail(ResultCode.FAILURE, "该文章不存在");
        }
        String pictureLink = articleDb.getPictureLink();
        try {
            if (ObjectUtil.isNotEmpty(pictureLink)) {
                for (String url : pictureLink.split(";")) {
                    aliyunOssService.deleteFile(url);
                }
            }
        } catch (Exception e) {
            log.error("文章删除上传的图片发生异常：{}", e.getMessage());
        }
        int operCount = userArticleMapper.delete(new LambdaQueryWrapper<UserArticle>()
                .eq(UserArticle::getAccountid, accountid).eq(UserArticle::getArticleid, articleid));
        if (articleMapper.deleteById(articleid) == operCount) {
            rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_DELETE_ROUTING, articleid);
            R.success(ResultCode.SUCCESS);
        }
        return R.fail(ResultCode.FAILURE);
    }


    /**
     * 用户我的模块中查询自己已发布或收藏、点赞的文章
     *
     * @param accountid
     * @param content
     * @param label
     * @return
     */
    @Override
    public R<List<Article>> vagueSelect(String accountid, String content, String label) {
        R<List<Article>> handleResult = null;
        if (label.equals(ARTICLE_TYPE)) {
            handleResult = userPubilshedArticle(accountid, content);
        }
        if (label.equals(ARTICLE_STAR_TYPE)) {
            handleResult = userStarArticles(accountid, content);
        }
        if (label.equals(ARTICLE_COLLECTION_TYPE)) {
            handleResult = getUserCollectionArticles(accountid, content);
        }
        if (handleResult == null) {
            return R.success("暂无记录");
        }
        return handleResult;
    }

    /**
     * 根据文章id更新文章浏览量
     *
     * @param articleid
     * @param size
     */
    @Override
    public boolean updateArticlePageview(String articleid, Long size) {
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getPageView)
                .eq(Article::getId, Long.valueOf(articleid)));
        Long newPageView = 0L;
        if ((ObjectUtil.isNotEmpty(article.getPageView())) && ((newPageView = (article.getPageView() + size)) > 0)) {
            article.setPageView(newPageView);
        } else {
            article.setPageView(size);
        }
        int updateCount = articleMapper.update(article, new LambdaQueryWrapper<Article>().eq(Article::getId,
                Long.valueOf(articleid)));
        if (!(updateCount > 0)) {
            log.error("文章id；{}浏览量更新失败，丢失浏览量：{}", articleid, size);
            return false;
        }
        return true;
    }

    /**
     * 根据文章id删除文章
     *
     * @return
     */
    @Override
    @Transactional
    public R deleteArticleById(Long articleid) {
        // 查询文章判断是否当前登录用户的文章
        UserArticle userArticle = userArticleMapper.selectOne(new LambdaQueryWrapper<UserArticle>()
                .select(UserArticle::getAccountid).eq(UserArticle::getArticleid, articleid));
        if (!LoginUser.getCurrentLoginUser().getAccountid().equals(userArticle.getAccountid())) {
            return R.fail(ResultCode.FAILURE, "用户只能删除自己发布的文章");
        }
        // 删除文章
        if (articleMapper.deleteById(articleid) > 0 && userArticleMapper.delete(new LambdaQueryWrapper<UserArticle>()
                .eq(UserArticle::getArticleid, articleid)) > 0) {
            // 删除索引库、点赞表、收藏表、评论表中的数据
            rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_DELETE_ROUTING, articleid.toString());
            return R.success(ResultCode.SUCCESS);
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 用户更新文章
     *
     * @param pictures
     * @param article
     * @param tags
     * @return
     */
    @Override
    public R putArticle(String[] pictures, Article article, String[] tags) {
        Article handledArticle = handleFieldPostOper(pictures, article, tags, article.getIsProduct());
        if (articleMapper.update(handledArticle, new LambdaQueryWrapper<Article>().eq(Article::getId,
                article.getId())) > 0) {
            // 更新es库中的信息
            rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_UPDATE_ROUTING, article.getId().toString());
            return R.success(ResultCode.SUCCESS);
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 删除相关数据，评论，回复，评论点赞，回复点赞，收藏
     *
     * @param articleid
     * @return
     */
    @Override
    @Transactional
    public void deleteRelatedData(String articleid) {
        // 删除评论，如果该文章评论存在则返回评论id
        Long commentId = articleCommentService.deleteRecord(articleid);
        if (!commentId.equals(0L)) {
            // 该评论存在，根据评论判断是否删除评论点赞，评论回复
            Long commentReplyId = articleReplyService.deleteRecord(commentId);
            if (!(commentReplyId.equals(0L))) {
                // 该评论回复存在，根据评论回复判断是否删除评论回复点赞
                articleCommentReplyStarService.deleteRecord(commentReplyId);
            }
        }
        // 删除收藏文章记录
        articleCollectionService.deleteRecord(articleid);
    }

    /**
     * 根据文章id判断当前登录用户是否点赞该文章
     *
     * @param accountid
     * @return
     */
    @Override
    public R<Map<String, String>> getLikeCollectStatus(String articleid) {
        String currentLoginUserAccountid = LoginUser.getCurrentLoginUser().getAccountid();
        Integer collectCount = articleCollectionMapper.selectCount(new LambdaQueryWrapper<ArticleCollection>()
                .eq(ArticleCollection::getAccountid, currentLoginUserAccountid)
                .eq(ArticleCollection::getArticleid, articleid));
        Integer starCount = articleStarMapper.selectCount(new LambdaQueryWrapper<ArticleStar>()
                .eq(ArticleStar::getAccountid, currentLoginUserAccountid)
                .eq(ArticleStar::getArticleid, articleid));

        Map<String, String> status = new HashMap<>();
        status.put("iscollect", collectCount.toString());
        status.put("isLike", starCount.toString());

        return R.data(status);
    }

    /**
     * 获取轮播图
     *
     * @return
     */
    @Override
    public R<List<ArticleSearchDTO>> shufflingfigure() throws IOException {

        List<ArticleSearchDTO> articleHandleResult = articleIndexService.shufflingfigure();
        return R.data(articleHandleResult);
    }

    /**
     * 处理图片集合，标签集合，并完成赋值实体
     *
     * @param pictures
     * @param article
     * @param tags
     * @return
     */
    private Article handleFieldPostOper(String[] pictures, Article article, String[] tags, Boolean isProduct) {
        String prictureLinkString = null;
        String tagString = null;

        if (ObjectUtil.isNotEmpty(pictures) && pictures.length > 0) {
            // 将集合转换为字符串
            prictureLinkString = org.apache.commons.lang.StringUtils.join(pictures, ";");
        }
        if (ObjectUtil.isNotEmpty(pictures) && tags.length > 0) {
            tagString = articleTagService.getIdsByTags(tags);
        }
        if (!ObjectUtil.isEmpty(article)) {
            article.setPictureLink(prictureLinkString);
            article.setTag(tagString);
            article.setIsProduct(isProduct);
        }

        return article;
    }


    /**
     * 如果该文章有收藏数，点赞数，浏览量则进行相加返回结果
     *
     * @param article
     * @return
     */
    private int getCountMerge(Article article) {
        int countMerge = 0;
        if (!ObjectUtil.hasEmpty(article.getCollectionCount())) {
            countMerge += article.getCollectionCount();
        }
        if (!ObjectUtil.hasEmpty(article.getStarCount())) {
            countMerge += article.getStarCount();
        }
        if (!ObjectUtil.hasEmpty(article.getPageView())) {
            countMerge += article.getPageView();
        }
        return countMerge;
    }

    /**
     * 根据id列表返回处理好的文字数据
     *
     * @param ids
     * @return
     */
    private List<Article> handleArticlesByIds(List<Long> ids, String keyword) {
        if (ids.size() == 0) {
            return null;
        }
        // 根据id列表查询所有文章并处理返回
        List<Article> articleDb = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getTitle, Article::getCreateTime, Article::getPictureLink)
                .in(Article::getId, ids).like(StringUtils.hasText(keyword), Article::getTitle, keyword));
        // 处理文章数据，填充点赞数，收藏数，图片字段分割
        return getArticlesHandleResult(articleDb);
    }

    /**
     * 处理文章数据，填充点赞数，收藏数，图片字段分割
     *
     * @param articlesDb
     * @return
     */
    private List<Article> getArticlesHandleResult(List<Article> articlesDb) {
        // 处理文章数据
        return articlesDb.stream().map(article -> {
            // 填充点赞数
            article.setStarCount(getArticleStarCount(article));
            // 填充收藏数
            article.setCollectionCount(getArticleCollectionCount(article));
            // 处理图片字段分割
            return divisionPictureLinkByOne(article);
        }).collect(Collectors.toList());
    }

    /**
     * 处理图片路径字段分割
     *
     * @param article
     * @return 返回一张图片
     */
    private Article divisionPictureLinkByOne(Article article) {
        // 分割图片链接
        List<String> linkList = UtilMethod.divisionLinkString(article.getPictureLink(), ";");
        if (!(CollectionUtils.isEmpty(linkList)) && linkList.size() > 0) {
            article.setPictureLink(linkList.get(0));
        }
        return article;
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
        article.setPictureLinks(linkList);
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

    /**
     * 筛选处理字段
     * 处理List<Article>转换为List<ArticleTypeDTO>
     *
     * @return
     */
    private List<ArticleTypeDTO> handleArticleField(List<Article> ArticlesDB) {
        List<ArticleTypeDTO> result = ArticlesDB.stream().map(article -> {
            article.setPictureLinks(UtilMethod.divisionLinkString(article.getPictureLink(), ";"));
            return UtilMethod.copyArticleIgnoreField(article);
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取室内外景区,过滤最近3个月外的数据，按照浏览量降序
     *
     * @param type
     * @param count
     * @param currentMonthValue
     * @param door
     * @return
     */
    private List<Article> getArticlesByDoor(Integer type, Integer count, int currentMonthValue, String door) {
        return articleMapper.selectList(new LambdaQueryWrapper<Article>().eq(Article::getType, type)
                .inSql(Article::getId, "select articleid from cs_article_attraction where scene = '" + door + "'")
                .between(Article::getRecommendMonth, currentMonthValue - 1, currentMonthValue + 1)
                .orderByDesc(Article::getPageView).last("limit " + count));
    }
}
