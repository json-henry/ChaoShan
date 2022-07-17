package com.chaoshan.mq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.chaoshan.constant.RabbitMQConstant.HOT_SEARCH_QUEUE_NAME;
import static com.chaoshan.constant.RabbitMQConstant.TOPIC_VIEW_QUEUE_NAME;
import static com.chaoshan.constant.RedisConstant.*;


/**
 * @author YCE
 * @date 2022/5/19
 * @Description: redis写入监听器
 */
@Component
public class RedisMqListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitListener(queuesToDeclare = @Queue(TOPIC_VIEW_QUEUE_NAME))
    public void addTopicViewCount(String jsonString) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonString);
        String accountId = jsonObject.getStr("accountId");
        String topicId = jsonObject.getStr("topicId");
        String userViewKey = TOPIC_USER_PREFIX + accountId + ":" + topicId;
        //获取用户访问标识
        String s = redisTemplate.opsForValue().get(userViewKey);
        //判断用户访问标识是否过期
        if (StrUtil.isEmpty(s)) {
            //给话题的浏览量+1
            redisTemplate.opsForZSet().incrementScore(TOPIC_VIEW_NUM_KEY, topicId, 1);
        }
        //用户在访问标识还没过期时继续重复访问的话会重置访问标识，防止恶意访问刷访问量
        redisTemplate.opsForValue().set(userViewKey, USER_IDENTIFY_VALUE, USER_IDENTIFY_EXPIRE_TIME,
                TimeUnit.SECONDS);
    }

    @RabbitListener(queuesToDeclare = @Queue(HOT_SEARCH_QUEUE_NAME))
    public void addHotSearchKeyCount(String jsonString) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonString);
        String accountId = jsonObject.getStr("accountId");
        String searchKey = jsonObject.getStr("searchKey");
        String userSearchIdentifyKey = HOT_SEARCH_USER_PREFIX + accountId + ":" + searchKey;
        //获取用户访问标识
        String s = redisTemplate.opsForValue().get(userSearchIdentifyKey);
        //判断用户访问标识是否过期
        if (StrUtil.isEmpty(s)) {
            //给搜索关键字的搜索次数+1
            redisTemplate.opsForZSet().incrementScore(HOT_SEARCH_KEY, searchKey, 1);
        }
        //用户在访问标识还没过期时继续重复访问的话会重置访问标识，防止恶意访问刷访问量
        redisTemplate.opsForValue().set(userSearchIdentifyKey, USER_IDENTIFY_VALUE, USER_IDENTIFY_EXPIRE_TIME,
                TimeUnit.SECONDS);
    }


}