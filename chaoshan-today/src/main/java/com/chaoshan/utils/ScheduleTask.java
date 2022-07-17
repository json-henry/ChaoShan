package com.chaoshan.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.constant.RedisConstant;
import com.chaoshan.entity.TodaySearchKey;
import com.chaoshan.entity.Topic;
import com.chaoshan.service.impl.TodaySearchKeyServiceImpl;
import com.chaoshan.service.impl.TopicServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author YCE
 */
@Component
@EnableScheduling
@Slf4j
public class ScheduleTask {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TodaySearchKeyServiceImpl searchKeyService;
    @Autowired
    private TopicServiceImpl topicService;

    /**
     * 定时任务，每隔30分钟执行一次
     *
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 0/30 * * * ? ")
    private void configureTasks() throws InterruptedException {
        log.info("定时任务启动...");
        TimeInterval timer = DateUtil.timer();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisConstant.TODAY_GLOBAL_LOCK))) {
            //如果全局锁存在，则取消执行本次定时任务，防止没必要的判断次数过多
            return;
        }
        todaySearchKeyFromRedisToMysql();
        topicViewCountFromRedisToMysql();
        log.info("定时任务结束，耗时：{}ms", timer.interval());
    }


    /**
     * 从redis中读出数据写入到mysql
     */
    public void topicViewCountFromRedisToMysql() {
        Set<ZSetOperations.TypedTuple<String>> set =
                redisTemplate.opsForZSet().rangeWithScores(RedisConstant.TOPIC_VIEW_NUM_KEY, 0, -1);
        ArrayList<ZSetOperations.TypedTuple<String>> tuples = new ArrayList<>(set);
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            topicService.updateById(new Topic().setId(Long.valueOf(tuple.getValue())).setViewCount(tuple.getScore().longValue()));
        }
    }

    /**
     * 从redis中读出数据写入到mysql
     */
    public void todaySearchKeyFromRedisToMysql() {
        Set<ZSetOperations.TypedTuple<String>> set =
                redisTemplate.opsForZSet().rangeWithScores(RedisConstant.HOT_SEARCH_KEY, 0, -1);
        ArrayList<ZSetOperations.TypedTuple<String>> tuples = new ArrayList<>(set);
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            TodaySearchKey searchKey =
                    searchKeyService.getOne(new LambdaQueryWrapper<TodaySearchKey>().eq(TodaySearchKey::getKeyWord,
                            tuple.getValue()));
            if (ObjectUtils.isEmpty(searchKey)) {
                searchKey = new TodaySearchKey()
                        .setKeyWord(tuple.getValue())
                        .setCount(tuple.getScore().longValue())
                        .setCreateBy("123");
            } else {
                searchKey.setCount(tuple.getScore().longValue());
            }
            searchKeyService.saveOrUpdate(searchKey);
        }
    }


}