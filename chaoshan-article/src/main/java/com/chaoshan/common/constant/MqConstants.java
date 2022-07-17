package com.chaoshan.common.constant;

/**
 * @DATE: 2022/05/17 15:37
 * @Author: 小爽帅到拖网速
 */
public class MqConstants {
    /**
     * 交换机
     */
    public final static String ARTICLE_EXCHANGE = "article";

    /**
     * 新增文章队列
     */
    public final static String ARTICLE_INSERT_QUEUE = "article_insert_queue";

    /**
     * 新增文章路由
     */
    public final static String ARTICLE_INSERT_ROUTING = "article:insert";

    /**
     * 删除文章队列
     */
    public final static String ARTICLE_DELETE_QUEUE = "article_delete_queue";

    /**
     * 删除文章路由
     */
    public final static String ARTICLE_DELETE_ROUTING = "article:delete";

    /**
     * 更新文章队列
     */
    public final static String ARTICLE_UPDATE_QUEUE = "article_update_queue";

    /**
     * 更新文章路由
     */
    public final static String ARTICLE_UPDATE_ROUTING = "article:update";

    /**
     * 新增消息队列
     */
    public final static String ARTICLE_MESSAGE_QUEUE = "article_message_insert";

    /**
     * 新增消息路由
     */
    public final static String ARTICLE_MESSAGE_INSERT_ROUTING = "article:message:insert";

    /**
     * 新增热搜词队列
     */
    public final static String ARTICLE_HOT_SEARCH_QUEUE = "article:hot:search:insert";

    /**
     * 新增热搜词路由
     */
    public final static String ARTICLE_HOT_SEARCH_ROUTING = "article_hot_search_insert";

    /**
     * 文章浏览量队列
     */
    public final static String ARTICLE_PAGEVIEW_QUEUE = "article:pageview:queue";
    /**
     * 文章浏览量路由
     */
    public final static String ARTICLE_PAGEVIEW_ROUTING = "article_pageview";


}
