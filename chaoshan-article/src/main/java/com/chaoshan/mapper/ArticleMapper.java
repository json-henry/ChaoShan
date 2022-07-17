package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chaoshan.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据类型分页获取数据
     *
     * @param type
     * @param articlePage
     * @return
     */
    IPage<Article> getArtPage(@Param("articlePage") Page<Article> articlePage, @Param("type") Integer type);

    /**
     * 每日推荐
     *
     * @return
     */
    List<Article> recommendationDaily();

    /**
     * 根据文章id返回文章详情
     *
     * @param id
     * @return
     */
    Article getArticleById(Integer id);

    /**
     * 用户查询已发布的文章,包括封面，标题，点赞数，收藏数，时间
     *
     * @param accountid
     * @return
     */
    List<Article> userPubilshedArticle(@Param("accountid") String accountid, @Param("keyword") String keyword);

    /**
     * 用户查询未发布的文章,包括封面，标题，点赞数，收藏数，时间
     *
     * @param accountid
     * @return
     */
    List<Article> getDrafts(@Param("accountid") String accountid, @Param("keyword") String keyword);
}