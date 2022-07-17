package com.chaoshan.controller;

import com.chaoshan.service.impl.TopicServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-16  10:53
 * @Version: 1.0
 */
@SpringBootTest
class TopicArticleControllerTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TopicServiceImpl service;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testZset() {
        Set<String> topic = stringRedisTemplate.opsForZSet().reverseRange("topic", 0, 15);
        List<String> list = new ArrayList<>(topic);
    }
}