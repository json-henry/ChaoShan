package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.TopicArticleReplyStar;
import com.chaoshan.util.api.R;

/**
 * @author YCE
 * @description 针对表【cs_topic_article_reply_star(话题文章回复点赞表)】的数据库操作Service
 * @createDate 2022-05-17 17:34:59
 */
public interface TopicArticleReplyStarService extends IService<TopicArticleReplyStar> {

    /**
     * 点赞评论
     *
     * @param replyId
     * @return
     */
    R starReply(Long replyId);
}
