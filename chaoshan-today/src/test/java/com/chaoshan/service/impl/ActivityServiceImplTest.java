package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.Activity;
import com.chaoshan.mapper.ActivityMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-18  13:42
 * @Version: 1.0
 */
@SpringBootTest
class ActivityServiceImplTest {

    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private ActivityMapper mapper;

    @Test
    void testActivityService() {
        List<Activity> list = activityService.getActivityListBySize(10);
        for (Activity activity : list) {
            System.out.println(activity);
        }
    }

    @Test
    void testGetList() {
        List<Activity> activityList =
                mapper.selectList(new LambdaQueryWrapper<Activity>().orderByAsc(Activity::getCreateTime));
        for (Activity activity : activityList) {
            System.out.println(activity);
        }
    }

    @Test
    void getActivityListBySize() {
        System.out.println(activityService.getActivityListBySize(100));
    }
}