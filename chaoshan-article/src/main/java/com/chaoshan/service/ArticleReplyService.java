package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ArticleReply;
import com.chaoshan.util.api.R;

/**
 * @DATE: 2022/05/12 09:02
 * @Author: 小爽帅到拖网速
 */

public interface ArticleReplyService extends IService<ArticleReply> {

    /**
     * 用户回复文章评论
     *
     * @param articleReply
     * @return
     */
    R replyCommentArticle(ArticleReply articleReply);

    /**
     * 删除评论回复
     *
     * @param commentId
     * @return
     */
    Long deleteRecord(Long commentId);

    /**
     * 根据评论id删除文章回复评论
     *
     * @param commentId
     * @return
     */
    R deleteReplyCommentArticle(Long commentId);
}
