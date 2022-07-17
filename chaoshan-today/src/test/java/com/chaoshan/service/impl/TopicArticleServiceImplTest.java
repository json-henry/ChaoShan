package com.chaoshan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.Topic;
import com.chaoshan.entity.TopicArticleStar;
import com.chaoshan.mapper.TopicArticleStarMapper;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.chaoshan.constant.ESConstant.TOPIC_INDEX;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-21  01:48
 * @Description: TopicArticleServiceImplTest
 * @Version: 1.0
 */
@SpringBootTest
class TopicArticleServiceImplTest {
    @Autowired
    private TopicArticleStarMapper starMapper;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private TopicServiceImpl topicService;

    @Test
    void test1() {
        String accountId = "123";
        TopicArticleStar star = starMapper.selectOne(new LambdaQueryWrapper<TopicArticleStar>()
                .eq(TopicArticleStar::getArticleId, 124)
                .eq(TopicArticleStar::getSendAccountid, accountId));
        if (!ObjectUtils.isEmpty(star)) {
            star.setIsStar(star.getIsStar() == 1 ? 0 : 1);
            starMapper.updateById(star);
        } else {
            star = new TopicArticleStar(accountId, 124L);
            starMapper.insert(star);
        }
    }

    @Test
    @SneakyThrows
    void test2() {
        SearchRequest request = new SearchRequest(TOPIC_INDEX);
        request.source()
                .query(QueryBuilders.matchQuery("all", "金字塔"))
                .sort("title", SortOrder.ASC);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<Topic> topicList = new ArrayList<>();
        for (SearchHit hit : hits) {
            Topic topic = JSON.parseObject(hit.getSourceAsString(), Topic.class);
            topicList.add(topic);
        }
        System.out.println(topicList);
    }

    @Test
    void test3() {
        List<Topic> list = topicService.list(new LambdaQueryWrapper<Topic>());
        System.out.println(list);
    }

    @Test
    void testInsertReturn() {
        System.out.println(starMapper.insert(new TopicArticleStar("123", 1L)));
    }
}