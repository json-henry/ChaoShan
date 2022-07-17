package com.chaoshan.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.constant.ESConstant;
import com.chaoshan.constant.RedisConstant;
import com.chaoshan.entity.Activity;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.entity.TodaySearchKey;
import com.chaoshan.entity.Topic;
import com.chaoshan.entity.doc.OpenscenicDoc;
import com.chaoshan.service.impl.ActivityServiceImpl;
import com.chaoshan.service.impl.OpenscenicServiceImpl;
import com.chaoshan.service.impl.TodaySearchKeyServiceImpl;
import com.chaoshan.service.impl.TopicServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-21  22:11
 * @Description: 今日模块启动时执行的工具
 * @Version: 1.0
 */

@Component
@Slf4j
public class ApplicationStartUtils implements ApplicationRunner {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private TopicServiceImpl topicService;
    @Autowired
    private OpenscenicServiceImpl scenicService;
    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private TodaySearchKeyServiceImpl searchKeyService;
    @Autowired
    private ScheduleTask scheduleTask;

    @PostConstruct
    public void start() {
        log.info("定义全局锁");
        redisTemplate.opsForValue().set(RedisConstant.TODAY_GLOBAL_LOCK, "");
    }

    @PreDestroy
    public void end() {
        redisTemplate.opsForValue().set(RedisConstant.TODAY_GLOBAL_LOCK, "");
        try {
            log.info("程序结束前保证redis和mysql数据一致");
            scheduleTask.todaySearchKeyFromRedisToMysql();
            scheduleTask.topicViewCountFromRedisToMysql();
            deleteKeysFromRedis();
        } finally {
            redisTemplate.delete(RedisConstant.TODAY_GLOBAL_LOCK);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("全局锁被使用中...");
        try {
            esInitialization();
            redisInitialization();
        } finally {
            redisTemplate.delete(RedisConstant.TODAY_GLOBAL_LOCK);
            log.info("全局锁释放");
        }
    }

    /**
     * 删除redis中的key
     */
    private void deleteKeysFromRedis() {
        redisTemplate.delete(RedisConstant.TOPIC_VIEW_NUM_KEY);
        redisTemplate.delete(RedisConstant.HOT_SEARCH_KEY);
    }


    /**
     * redis初始化
     */
    private void redisInitialization() {
        topicViewCountFromMysqlToRedis();
        todaySearchKeyFromMysqlToRedis();
    }

    /**
     * 从数据库中读出数据并写入redis
     */
    private void topicViewCountFromMysqlToRedis() {
        List<Topic> list = topicService.list(new LambdaQueryWrapper<>());
        for (Topic topic : list) {
            redisTemplate.opsForZSet().incrementScore(RedisConstant.TOPIC_VIEW_NUM_KEY, topic.getId().toString(),
                    topic.getViewCount());
        }
    }

    /**
     * 从数据库中读出数据并写入redis
     */
    private void todaySearchKeyFromMysqlToRedis() {
        List<TodaySearchKey> list = searchKeyService.list(new LambdaQueryWrapper<>());
        for (TodaySearchKey searchKey : list) {
            redisTemplate.opsForZSet().incrementScore(RedisConstant.HOT_SEARCH_KEY, searchKey.getKeyWord(),
                    searchKey.getCount());
        }
    }


    private void esInitialization() throws IOException {
        GetIndexRequest topicRequest = new GetIndexRequest(ESConstant.TOPIC_INDEX);
        GetIndexRequest scenicRequest = new GetIndexRequest(ESConstant.SCENIC_INDEX);
        GetIndexRequest activityRequest = new GetIndexRequest(ESConstant.ACTIVITY_INDEX);
        boolean topicExists = client.indices().exists(topicRequest, RequestOptions.DEFAULT);
        boolean scenicExists = client.indices().exists(scenicRequest, RequestOptions.DEFAULT);
        boolean activityExists = client.indices().exists(activityRequest, RequestOptions.DEFAULT);

        if (!topicExists) {
            log.info(ESConstant.TOPIC_INDEX + "索引库不存在，正在创建该索引库中...");
            createTopicIndexAndWriteData();
        }
        if (!scenicExists) {
            log.info(ESConstant.SCENIC_INDEX + "索引库不存在，正在创建该索引库中...");
            createScenicIndexAndWriteData();
        }
        if (!activityExists) {
            log.info(ESConstant.ACTIVITY_INDEX + "索引库不存在，正在创建该索引库中...");
            createActivityIndexAndWriteData();
        }

    }

    private void createTopicIndexAndWriteData() throws IOException {
        //创建索引库
        CreateIndexRequest request = new CreateIndexRequest(ESConstant.TOPIC_INDEX);
        request.source(ESConstant.TOPIC_MAPPING_TEMPLATE, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
        //从数据库中获取数据后批量写入索引库中
        List<Topic> topicList = topicService.list(new LambdaQueryWrapper<>());
        if (CollectionUtils.isEmpty(topicList)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (Topic topic : topicList) {
            bulkRequest.add(new IndexRequest(ESConstant.TOPIC_INDEX)
                    .id(topic.getId().toString())
                    .source(JSON.toJSONString(topic), XContentType.JSON));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    private void createScenicIndexAndWriteData() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(ESConstant.SCENIC_INDEX);
        request.source(ESConstant.SCENIC_MAPPING_TEMPLATE, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);

        //从数据库中获取数据后批量写入索引库中
        List<Openscenic> scenicList = scenicService.list(new LambdaQueryWrapper<>());
        BulkRequest bulkRequest = new BulkRequest();
        for (Openscenic scenic : scenicList) {
            OpenscenicDoc scenicDoc = new OpenscenicDoc(scenic);
            bulkRequest.add(new IndexRequest(ESConstant.SCENIC_INDEX)
                    .id(scenicDoc.getId().toString())
                    .source(JSON.toJSONString(scenicDoc), XContentType.JSON));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    private void createActivityIndexAndWriteData() throws IOException {
        //创建索引库
        CreateIndexRequest request = new CreateIndexRequest(ESConstant.ACTIVITY_INDEX);
        request.source(ESConstant.ACTIVITY_MAPPING_TEMPLATE, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);

        //从数据库中获取数据后批量写入索引库中
        List<Activity> activityList = activityService.list(new LambdaQueryWrapper<>());
        if (CollectionUtils.isEmpty(activityList)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (Activity activity : activityList) {
            bulkRequest.add(new IndexRequest(ESConstant.ACTIVITY_INDEX)
                    .id(activity.getId().toString())
                    .source(JSON.toJSONString(activity), XContentType.JSON));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }


}
