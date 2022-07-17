package com.chaoshan.mq;

import com.alibaba.fastjson.JSON;
import com.chaoshan.constant.ESConstant;
import com.chaoshan.entity.Activity;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.entity.Topic;
import com.chaoshan.service.impl.ActivityServiceImpl;
import com.chaoshan.service.impl.OpenscenicServiceImpl;
import com.chaoshan.service.impl.TopicServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

import static com.chaoshan.constant.RabbitMQConstant.*;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-22  11:08
 * @Description: es写入监听器
 * @Version: 1.0
 */
@Component
@Slf4j
public class EsMqListener {

    @Autowired
    private TopicServiceImpl topicService;
    @Autowired
    private OpenscenicServiceImpl scenicService;
    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private RestHighLevelClient client;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = TOPIC_INSERT_QUEUE),
            exchange = @Exchange(name = TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = TOPIC_INSERT_KEY
    ))
    public void listenerTopicInsertAndUpdate(Long id) throws IOException {
        if (ObjectUtils.isEmpty(id)) {
            log.info("topicId为空，无法同步数据到ES");
            return;
        }
        Topic topic = topicService.getById(id);
        if (ObjectUtils.isEmpty(topic)) {
            log.info("查询不到id为{}的话题,无法同步到ES", id);
            return;
        }
        IndexRequest request = new IndexRequest(ESConstant.TOPIC_INDEX).id(topic.getId().toString());
        request.source(JSON.toJSONString(topic), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = TOPIC_DELETE_QUEUE),
            exchange = @Exchange(name = TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = TOPIC_DELETE_KEY
    ))
    public void listenerTopicDelete(Long id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(ESConstant.TOPIC_INDEX, id.toString());
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SCENIC_INSERT_QUEUE),
            exchange = @Exchange(name = SCENIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = SCENIC_INSERT_KEY
    ))
    public void listenerScenicInsertAndUpdate(Long id) throws IOException {
        if (ObjectUtils.isEmpty(id)) {
            log.info("ScenicId为空，无法同步数据到ES");
            return;
        }
        Openscenic scenic = scenicService.getById(id);
        if (ObjectUtils.isEmpty(scenic)) {
            log.info("查询不到id为{}的话题,无法同步到ES", id);
            return;
        }
        IndexRequest request = new IndexRequest(ESConstant.SCENIC_INDEX).id(scenic.getId().toString());
        request.source(JSON.toJSONString(scenic), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SCENIC_DELETE_QUEUE),
            exchange = @Exchange(name = SCENIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = SCENIC_DELETE_KEY
    ))
    public void listenerScenicDelete(Long id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(ESConstant.SCENIC_INDEX, id.toString());
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ACTIVITY_INSERT_QUEUE),
            exchange = @Exchange(name = ACTIVITY_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ACTIVITY_INSERT_KEY
    ))
    public void listenerActivityInsertAndUpdate(Long id) throws IOException {
        if (ObjectUtils.isEmpty(id)) {
            log.info("activityId为空，无法同步数据到ES");
            return;
        }
        Activity activity = activityService.getById(id);
        if (ObjectUtils.isEmpty(activity)) {
            log.info("查询不到id为{}的话题,无法同步到ES", id);
            return;
        }
        IndexRequest request = new IndexRequest(ESConstant.ACTIVITY_INDEX).id(activity.getId().toString());
        request.source(JSON.toJSONString(activity), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ACTIVITY_DELETE_QUEUE),
            exchange = @Exchange(name = ACTIVITY_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ACTIVITY_DELETE_KEY
    ))
    public void listenerActivityDelete(Long id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(ESConstant.ACTIVITY_INDEX, id.toString());
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }


}
