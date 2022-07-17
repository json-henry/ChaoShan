package com.chaoshan.constant;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-19  23:20
 * @Description: Redis相关常量
 * @Version: 1.0
 */
public class RedisConstant {


    public static final String TODAY_GLOBAL_LOCK = "today:lock";

    public static final String TOPIC_VIEW_NUM_KEY = "topic:viewNum";
    public static final String TOPIC_USER_PREFIX = "topic:user:";


    public static final String USER_IDENTIFY_VALUE = "666";
    public static final Integer USER_IDENTIFY_EXPIRE_TIME = 60;


    public static final String HOT_SEARCH_KEY = "today:hotSearch";
    public static final String HOT_SEARCH_USER_PREFIX = "today:hotSearch:user:";


}
