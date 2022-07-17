package com.chaoshan.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-14  12:36
 * @Description: 消息相关常量
 * @Version: 1.0
 */
public class MessageConstant {

    public static final Map<Integer, String> DESCRIPTION = new HashMap<>();

    public static final Integer ZERO = 0;
    public static final Integer ACTIVITY_MESSAGE = 0;
    public static final Integer TOPIC_MESSAGE = 1;
    public static final Integer ARTICLE_COMMENT_MESSAGE = 2;
    public static final Integer ARTICLE_REPLY_MESSAGE = 3;
    public static final Integer ARTICLE_STAR_MESSAGE = 4;
    public static final Integer TOPIC_COMMENT_MESSAGE = 5;
    public static final Integer TOPIC_REPLY_MESSAGE = 6;
    public static final Integer TOPIC_STAR_MESSAGE = 7;
    public static final Integer ARTICLE_COLLECTION_MESSAGE = 8;
    public static final Integer ARTICLE_STAR_COMMENT_MESSAGE = 9;
    public static final Integer TOPIC_STAR_COMMENT_MESSAGE = 10;
    public static final Integer FOCUS_ACCOUNTID = 11;

    public static final String MESSAGE_EXCHANGE = "message.topic";

    public static final String MESSAGE_INSERT_KEY = "message.insert";
    public static final String MESSAGE_INSERT_QUEUE = "message.insert.queue";

    public static final String MESSAGE_INSERT_ALL_KEY = "message.insertAll";
    public static final String MESSAGE_INSERT_ALL_QUEUE = "message.insertAll.queue";

    public static final String MESSAGE_READ_KEY = "message.read";
    public static final String MESSAGE_READ_QUEUE = "message.queue";


    static {
        DESCRIPTION.put(0, "activityNum");
        DESCRIPTION.put(1, "topicNum");
        DESCRIPTION.put(2, "commentAndReplyNum");
        DESCRIPTION.put(3, "commentAndReplyNum");
        DESCRIPTION.put(4, "starAndCollectionNum");
        DESCRIPTION.put(5, "commentAndReplyNum");
        DESCRIPTION.put(6, "commentAndReplyNum");
        DESCRIPTION.put(7, "starAndCollectionNum");
        DESCRIPTION.put(8, "starAndCollectionNum");
        DESCRIPTION.put(9, "starAndCollectionNum");
        DESCRIPTION.put(10, "starAndCollectionNum");
        DESCRIPTION.put(11, "focusAccountNum");
    }
}
