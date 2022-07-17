package com.chaoshan.service.search;

import com.chaoshan.entity.dto.ArticleSearchDTO;
import com.chaoshan.util.api.R;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @DATE: 2022/05/15 21:50
 * @Author: 小爽帅到拖网速
 */
public interface ArticleIndexService {

    /**
     * 创建article索引
     */
    boolean createArticleIndex();

    /**
     * 批量填充数据
     */
    R fillBatchArticleData(String ids);

    /**
     * 填充数据
     *
     * @param articleids
     * @return
     */
    R fillArticleData(String articleids);

    /**
     * 删除单条数据
     *
     * @param articleid
     * @return
     */
    R deleteArticleData(String articleid);

    /**
     * 更新单条数据
     *
     * @param articleid
     * @return
     */
    R updateArticleData(String articleid);

    /**
     * 根据关键字以及类型进行搜索,并将搜素结果中的关键字高亮返回
     *
     * @param keyword
     * @param type
     * @return
     */
    R<Map<String, Object>> articleSearch(String keyword, Integer type);

    /**
     * 相关查询返回
     *
     * @param keyword
     * @param type
     * @return
     */
    List<ArticleSearchDTO> articleRealted(String keyword);

    /**
     * 综合4个模块返回前8篇推荐文章
     *
     * @param type
     * @return
     */
    List<ArticleSearchDTO> recommendationDaily();

    /**
     * 热搜
     *
     * @return
     */
    R<List<Map<String, Object>>> articleHotSearch();

    /**
     * 定时更新索引库中的浏览量，点赞数，收藏数
     */
    void taskUpdateArticleIndex();

    /**
     * 填充热搜值
     */
    void fillHotSearchValue();


    /**
     * 获取轮播图
     *
     * @return
     */
    List<ArticleSearchDTO> shufflingfigure() throws IOException;
}
