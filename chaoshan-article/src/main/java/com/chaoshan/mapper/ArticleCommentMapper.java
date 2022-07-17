package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.ArticleComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@Mapper
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {
    /**
     * 根据评论id 查询评论
     *
     * @param id
     * @return
     */
    List<ArticleComment> getCommentByArticleId(@Param("id") Long id);
}