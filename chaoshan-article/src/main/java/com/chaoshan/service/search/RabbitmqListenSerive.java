package com.chaoshan.service.search;

/**
 * @DATE: 2022/05/17 15:52
 * @Author: 小爽帅到拖网速
 */
public interface RabbitmqListenSerive {

    /**
     * 新增文章
     *
     * @param accountid
     */
    void articleInsert(String accountid);

    /**
     * 删除文章
     *
     * @param articleid
     */
    void articleDelete(String articleid);

    /**
     * 更新文章
     *
     * @param accountid
     */
    void articleUpdate(String accountid);

    // /**
    //  * 插入消息
    //  */
    // void articleMessageInsert(String messagJsonStr);

    /**
     * 新增热搜词
     */
    void insertHotSearch(String keyword);

    /**
     * 文章浏览量自增
     */
    void addArticlePageview(String mapStr);

}
