package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ArticleComment;
import com.chaoshan.util.api.R;

import java.util.List;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

public interface ArticleCommentService extends IService<ArticleComment> {


    /**
     * 根据文章id获取文章评论
     *
     * @param id
     * @return
     */
    List<ArticleComment> getCommentByArticleId(Long id);

    /**
     * 用户评论文章
     *
     * @param articleComment
     * @return
     */
    R commentArticle(ArticleComment articleComment);

    /**
     * 删除评论记录
     */
    Long deleteRecord(String articleid);

    /**
     * 根据评论id删除文章评论
     *
     * @param commentId
     * @return
     */
    R deleteCommentArticle(Long commentId);


}
