package com.chaoshan.service;

import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-18  23:22
 * @Version: 1.0
 */
public interface RedisService {

    int incrementScoreByUserId(String searchkey);

    List<String> getHotList(String searchkey);

    int incrementScore(String searchkey);


}
