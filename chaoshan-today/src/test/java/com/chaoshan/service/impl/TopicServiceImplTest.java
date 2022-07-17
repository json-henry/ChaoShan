package com.chaoshan.service.impl;

import com.chaoshan.entity.Topic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-18  17:34
 * @Version: 1.0
 */
@SpringBootTest
class TopicServiceImplTest {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TopicServiceImpl topicService;


    @Test
    void name() {
        topicService.updateById(new Topic().setId(2L).setActivityid(2L).setTitle("我真的被修改了"));
    }
}