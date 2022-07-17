package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ArticleCommentReplyStar;
import com.chaoshan.util.api.R;

/**
 * @DATE: 2022/05/14 08:33
 * @Author: 小爽帅到拖网速
 */

public interface ArticleCommentReplyStarService extends IService<ArticleCommentReplyStar> {

    /**
     * 用户对文章评论回复的点赞操作，根据请求会判断当前是否点赞，取反操作
     *
     * @param articleCommentReplyStar
     * @return
     */
    R starArticleCommentReplyOper(ArticleCommentReplyStar articleCommentReplyStar);

    /**
     * 删除评论回复点赞记录
     *
     * @param commentReplyId
     */
    void deleteRecord(Long commentReplyId);
}
