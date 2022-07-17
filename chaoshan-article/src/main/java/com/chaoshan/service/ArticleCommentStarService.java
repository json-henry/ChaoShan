package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ArticleCommentStar;
import com.chaoshan.util.api.R;

/**
 * @DATE: 2022/05/13 14:39
 * @Author: 小爽帅到拖网速
 */

public interface ArticleCommentStarService extends IService<ArticleCommentStar> {

    /**
     * 用户对文章评论的点赞操作，根据请求会去判断当前是否点赞，取反操作
     *
     * @param commentId
     * @return
     */
    R starArticleCommentOper(Long commentId);
}
