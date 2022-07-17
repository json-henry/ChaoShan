package com.chaoshan.service.impl;

import com.chaoshan.entity.TodaySearchKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-22  00:24
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootTest
class TodaySearchKeyServiceImplTest {

    @Autowired
    private TodaySearchKeyServiceImpl service;

    @Test
    void name() {
        service.updateCountByKeyword(new TodaySearchKey().setCount(666L).setKeyWord("123"));
    }
}