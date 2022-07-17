package com.chaoshan.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-19  11:08
 * @Version: 1.0
 */
@SpringBootTest
class RedisServiceImplTest {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    void testZSet() {
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
        System.out.println(zSet.score("hotSearch", "我爱你")); //0.0
        System.out.println(zSet.score("hotSearch", "我爱")); //null

    }
}