package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.TopicArticleCommentStar;
import com.chaoshan.util.api.R;

/**
 * @author YCE
 * @description 针对表【cs_topic_article_comment_star(话题文章评论点赞表 )】的数据库操作Service
 * @createDate 2022-05-17 17:34:59
 */
public interface TopicArticleCommentStarService extends IService<TopicArticleCommentStar> {
    /**
     * 点赞评论
     *
     * @param commentId
     * @return
     */
    R starComment(Long commentId);
}
