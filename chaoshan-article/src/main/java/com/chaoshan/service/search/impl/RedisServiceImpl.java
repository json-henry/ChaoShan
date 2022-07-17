package com.chaoshan.service.search.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.chaoshan.service.ArticleService;
import com.chaoshan.service.search.RedisService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.chaoshan.common.constant.ArticleRedisConstant.*;

/**
 * @DATE: 2022/05/18 21:49
 * @Author: 小爽帅到拖网速
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ArticleService articleService;

    /**
     * 新增搜索记录
     *
     * @param accountid
     * @param searchkey
     * @return
     */
    @Override
    public int addSearchHistoryByUserId(String accountid, String searchkey) {

        return 0;
    }

    /**
     * 新增一条热词搜索记录，将用户输入的热词存储下来
     *
     * @param searchkey
     * @return
     */
    @Override
    public void incrementScoreByUserId(List<String> searchkeys) {
        long now = System.currentTimeMillis();
        ZSetOperations zSetOperations = stringRedisTemplate.opsForZSet();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        List<Integer> integers = searchkeys.stream().map(searchkey -> {
            try {
                // 判断redis中是否存在该关键词
                if (zSetOperations.score(ARTICLE_HOT_KEY, searchkey) <= 0) {
                    // 不存在则添加关键词，默认分数为0
                    zSetOperations.add(ARTICLE_HOT_KEY, searchkey, 0);
                    valueOperations.set(ARTICLE_HOT_VAL_KEY + searchkey, String.valueOf(now));
                }
            } catch (Exception e) {
                zSetOperations.add(ARTICLE_HOT_KEY, searchkey, 0);
                valueOperations.set(ARTICLE_HOT_VAL_KEY + searchkey, String.valueOf(now));
            }
            return 1;
        }).collect(Collectors.toList());
    }

    /**
     * 根据searchkey搜索其相关最热的前十名 (如果searchkey为null空，则返回redis存储的前十最热词条)
     *
     * @param searchkey
     * @return
     */
    @Override
    public List<Map<String, Object>> getHotList(String searchkey) {
        String key = searchkey;
        long now = System.currentTimeMillis();
        List<Map<String, Object>> result = new ArrayList<>();
        ZSetOperations zSetOperations = stringRedisTemplate.opsForZSet();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        // 获取得分在参数2与参数3之前从高到低排序的排序集
        Set<ZSetOperations.TypedTuple<String>> dataSet = zSetOperations.reverseRangeByScoreWithScores(ARTICLE_HOT_KEY
                , 0, Double.MAX_VALUE);

        Iterator<ZSetOperations.TypedTuple<String>> iterator = dataSet.iterator();
        // keyword不为空的时候，推荐相关的最热前十名
        if (StringUtils.isNotEmpty(searchkey)) {
            while (dataSet.iterator().hasNext()) {
                ZSetOperations.TypedTuple<String> next = iterator.next();
                if (StringUtils.containsIgnoreCase(next.getValue(), searchkey)) {
                    // 只返回前10条热搜消息
                    if (result.size() > 9) {
                        break;
                    }
                    Long time = Long.valueOf(valueOperations.get(ARTICLE_HOT_VAL_KEY + next.getValue()));
                    if (now - time < 2592000000L) {
                        // 返回最近一个月的数据
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("name", next.getValue());
                        map.put("score", next.getScore());
                        result.add(map);
                    } else {
                        // 时间超过一个月没搜索就把这个词热度归为0
                        zSetOperations.add(ARTICLE_HOT_KEY, next.getValue(), 0);
                    }
                }
            }
        } else {
            while (iterator.hasNext()) {
                if (result.size() > 9) {
                    // 只返回前9条
                    break;
                }
                ZSetOperations.TypedTuple<String> next = iterator.next();
                Long time = Long.valueOf(valueOperations.get(ARTICLE_HOT_VAL_KEY + next.getValue()));
                if (now - time < 2592000000L) {
                    // 返回最近一个月的数据
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", next.getValue());
                    map.put("score", next.getScore());
                    result.add(map);
                } else {
                    // 时间超过一个月没搜索就把这个词热度归为0
                    zSetOperations.add(ARTICLE_HOT_KEY, next.getValue(), 0);
                }
            }
        }
        return result;
    }

    /**
     * 每次点击给相关词searchkey热度 +1
     *
     * @param searchkey
     * @return
     */
    @Override
    public int incrementScore(String searchkey) {
        long now = System.currentTimeMillis();
        ZSetOperations zSetOperations = stringRedisTemplate.opsForZSet();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        zSetOperations.incrementScore(ARTICLE_HOT_KEY, searchkey, 1);
        valueOperations.getAndSet(ARTICLE_HOT_VAL_KEY + searchkey, String.valueOf(now));
        return 1;
    }

    /**
     * 文章浏览量自增
     *
     * @param mapStr
     */
    @Override
    public void articlePageviewIncrement(String mapStr) {
        Map map = JSONUtil.toBean(mapStr, Map.class);
        String ipAddr = map.get("ip").toString();
        String articleid = map.get("articleid").toString();
        if (ObjectUtil.hasEmpty(ipAddr, articleid)) {
            log.error("ip地址或者文章id为空！");
            return;
        }
        String key = ARTICLE_ID_KEY + articleid;
        String key_copy = PREFIX + ARTICLE_ID_KEY + articleid;
        // 添加2次缓存
        Long status = stringRedisTemplate.opsForHyperLogLog().add(key, ipAddr);
        // 设置过期时间
        stringRedisTemplate.expire(key, 30, TimeUnit.SECONDS);
        stringRedisTemplate.opsForHyperLogLog().add(key_copy, ipAddr);
        if (status == 0) {
            log.info("该ip地址：{}重复访问文章id:{}", ipAddr, articleid);
        }
    }

    /**
     * 根据文章id查询文章在redis缓存中1小时的访问量
     *
     * @param articleid
     */
    @Override
    public Long getPageViewByArticleid(Long articleid) {

        return stringRedisTemplate.opsForHyperLogLog().size(ARTICLE_ID_KEY + articleid);
    }

    /**
     * 添加热搜关键字
     *
     * @param keywords
     * @return
     */
    @Override
    public R addHotSearchKeyword(String[] keywords) {
        List<String> list = Arrays.asList(keywords);
        long now = System.currentTimeMillis();
        ZSetOperations zSetOperations = stringRedisTemplate.opsForZSet();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        try {
            for (String keyword : keywords) {
                zSetOperations.incrementScore(ARTICLE_HOT_KEY, keyword, 1);
                valueOperations.getAndSet(ARTICLE_HOT_VAL_KEY + keyword, String.valueOf(now));
            }
        } catch (Exception e) {
            return R.fail(ResultCode.FAILURE);
        }
        return R.success(ResultCode.SUCCESS);
    }

    /**
     * 定时更新文章浏览量
     */
    @Override
    @Scheduled(cron = "0 0 2 * * ?") // 每天2:00更新
    // @Scheduled(cron = "0 0/2 * * * ?")
    public void scheduledUpdatePageview() {
        log.error("检查是否更新文章");
        Set<String> keys = stringRedisTemplate.keys(PREFIX + ARTICLE_ID_KEY + "*");
        for (String key : keys) {
            // 获取文章id
            String articleid = key.substring(PREFIX.length() + ARTICLE_ID_KEY.length());
            log.error("更新文章：{}", articleid);
            // 获取文章浏览量
            Long size = stringRedisTemplate.opsForHyperLogLog().size(key);
            if (articleService.updateArticlePageview(articleid, size)) {
                stringRedisTemplate.delete(key);
                stringRedisTemplate.delete(key.substring(PREFIX.length()));
            }
        }
    }

}
