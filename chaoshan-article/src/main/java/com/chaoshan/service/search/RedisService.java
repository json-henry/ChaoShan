package com.chaoshan.service.search;

import com.chaoshan.util.api.R;

import java.util.List;
import java.util.Map;

/**
 * @DATE: 2022/05/18 21:41
 * @Author: 小爽帅到拖网速
 */
public interface RedisService {

    /**
     * 新增搜索记录
     *
     * @param userid
     * @param searchkey
     * @return
     */
    int addSearchHistoryByUserId(String userid, String searchkey);

    /**
     * 新增一条热词搜索记录，将用户输入的热词存储下来
     *
     * @param searchkeys
     * @return
     */
    void incrementScoreByUserId(List<String> searchkeys);

    /**
     * 根据searchkey搜索其相关最热的前十名 (如果searchkey为null空，则返回redis存储的前十最热词条)
     *
     * @param searchkey
     * @return
     */
    List<Map<String, Object>> getHotList(String searchkey);

    /**
     * 每次点击给相关词searchkey热度 +1
     *
     * @param searchkey
     * @return
     */
    int incrementScore(String searchkey);


    /**
     * 文章浏览量自增
     *
     * @param mapStr
     */
    void articlePageviewIncrement(String mapStr);

    /**
     * 根据文章id查询文章在redis缓存中1小时的访问量
     *
     * @param articleid
     */
    Long getPageViewByArticleid(Long articleid);

    /**
     * 添加热搜关键字
     *
     * @param keywords
     * @return
     */
    R addHotSearchKeyword(String[] keywords);

    /**
     * 定时更新文章浏览量
     */
    void scheduledUpdatePageview();
}
