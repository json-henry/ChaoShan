package com.chaoshan.constant;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-20  13:38
 * @Description: 消息队列相关的参量
 * @Version: 1.0
 */
public class RabbitMQConstant {
    public static final String TOPIC_VIEW_QUEUE_NAME = "topic.viewCount";
    public static final String HOT_SEARCH_QUEUE_NAME = "today.hotSearch";

    public static final String EXCHANGE_PREFIX_ = "today.";
    public static final String TOPIC_EXCHANGE = EXCHANGE_PREFIX_ + "topic.topic";
    public static final String SCENIC_EXCHANGE = EXCHANGE_PREFIX_ + "scenic.topic";
    public static final String ACTIVITY_EXCHANGE = EXCHANGE_PREFIX_ + "activity.topic";

    public static final String TOPIC_INSERT_QUEUE = "topic.insert.queue";
    public static final String TOPIC_DELETE_QUEUE = "topic.delete.queue";
    public static final String SCENIC_INSERT_QUEUE = "scenic.insert.queue";
    public static final String SCENIC_DELETE_QUEUE = "scenic.delete.queue";
    public static final String ACTIVITY_INSERT_QUEUE = "activity.insert.queue";
    public static final String ACTIVITY_DELETE_QUEUE = "activity.delete.queue";

    public static final String TOPIC_INSERT_KEY = "topic.insert";
    public static final String TOPIC_DELETE_KEY = "topic.delete";
    public static final String SCENIC_INSERT_KEY = "scenic.insert";
    public static final String SCENIC_DELETE_KEY = "scenic.delete";
    public static final String ACTIVITY_INSERT_KEY = "activity.insert";
    public static final String ACTIVITY_DELETE_KEY = "activity.delete";

}
