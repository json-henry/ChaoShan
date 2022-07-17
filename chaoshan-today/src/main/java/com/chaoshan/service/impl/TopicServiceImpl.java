package com.chaoshan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.Topic;
import com.chaoshan.entity.TopicArticle;
import com.chaoshan.mapper.TopicArticleMapper;
import com.chaoshan.mapper.TopicMapper;
import com.chaoshan.service.TopicService;
import com.chaoshan.utils.DateTimeUtils;
import com.chaoshan.vo.TopicArticleVO;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.chaoshan.constant.ESConstant.TOPIC_INDEX;
import static com.chaoshan.constant.RedisConstant.TOPIC_VIEW_NUM_KEY;
import static com.chaoshan.constant.TodayConstant.TOPIC_HOT_COUNT;

/**
 * @author YCE
 * @description 针对表【cs_topic(话题表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:34:59
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
        implements TopicService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicArticleMapper topicArticleMapper;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private TopicArticleServiceImpl topicArticleService;


    /**
     * 根据话题id获取 话题和话题文章的相关内容
     */
    @Override
    @Transactional
    public Map<String, Object> getTopicAndArticleById(Long id) {
        Map<String, Object> map = new HashMap<>();
        Topic topic = topicMapper.selectById(id);
        if (ObjectUtils.isEmpty(topic)) {
            return null;
        }
        List<TopicArticle> topicArticles =
                topicArticleMapper.selectList(new LambdaQueryWrapper<TopicArticle>()
                        .eq(TopicArticle::getTopicId, id)
                        .orderByDesc(TopicArticle::getCreateTime));
        List<TopicArticleVO> topicArticleVOList = getTopicArticleVOList(topicArticles);
        map.put("topic", topic);
        map.put("topic_articles", topicArticleVOList);
        return map;
    }

    /**
     * 抽取方法，根据用户的话题文章获取对应的点赞数和评论数
     */
    private List<TopicArticleVO> getTopicArticleVOList(List<TopicArticle> topicArticles) {
        List<TopicArticleVO> topicArticleVos = new ArrayList<>();
        for (TopicArticle topicArticle : topicArticles) {
            TopicArticleVO topicArticleVO =
                    topicArticleService.getTopicArticleVoByTopicArticle(topicArticle);
            if (!ObjectUtils.isEmpty(topicArticle)) {
                topicArticleVos.add(topicArticleVO);
            }
        }
        return topicArticleVos;
    }

    /**
     * 获取热度排行靠前几的话题
     */
    @Override
    public List<Topic> getTopicListByHotSort(int size) {
        //从redis中获取排名靠前#size的话题id和对应的热度值
        Set<ZSetOperations.TypedTuple<String>> hots =
                redisTemplate.opsForZSet().reverseRangeWithScores(TOPIC_VIEW_NUM_KEY, 0, size);
        if (CollectionUtils.isEmpty(hots)) {
            return null;
        }
        List<ZSetOperations.TypedTuple<String>> hotList = new ArrayList<>(hots);
        List<Long> ids =
                hotList.stream().map(hot -> Long.valueOf(hot.getValue())).collect(Collectors.toList());
        List<Long> scores = hotList.stream().map(hot -> hot.getScore().longValue()).collect(Collectors.toList());
        List<Topic> topics = topicMapper.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(topics)) {
            return null;
        }
        for (int i = 0; i < topics.size(); i++) {
            Topic topic = topics.get(i);
            //判断创建时间是否在今天，是则加“新”标签
            topic.setIsNew(DateTimeUtils.isToday(topic.getCreateTime()));
            //判断热度是否超过限定值，是则加”热“标签
            topic.setIsHot(scores.get(i) >= TOPIC_HOT_COUNT);
        }
        return topics;
    }

    /**
     * 根据关键字搜索话题列表
     *
     * @param searchKey
     * @return
     */
    @Override
    @SneakyThrows
    public List<Topic> getTopicListBySearchKey(String searchKey) {
        SearchRequest request = new SearchRequest(TOPIC_INDEX);
        request.source()
                .query(QueryBuilders.matchQuery("all", searchKey));
        return getTopics(request);
    }

    /**
     * 返回所有的话题数据
     *
     * @return
     */
    @SneakyThrows
    public List<Topic> getTopicList() {
        SearchRequest request = new SearchRequest(TOPIC_INDEX);
        request.source()
                .query(QueryBuilders.matchAllQuery());
        return getTopics(request);
    }

    private List<Topic> getTopics(SearchRequest request) throws IOException {
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<Topic> topicList = new ArrayList<>();
        for (SearchHit hit : hits) {
            Topic topic = JSON.parseObject(hit.getSourceAsString(), Topic.class);
            long l = redisTemplate.opsForZSet().rank(TOPIC_VIEW_NUM_KEY, topic.getId().toString()).longValue();
            topic.setIsHot(l >= TOPIC_HOT_COUNT);
            topic.setIsNew(DateTimeUtils.isToday(topic.getCreateTime()));
            topicList.add(topic);
        }
        return topicList;
    }

}




