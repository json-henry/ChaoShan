package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.TopicArticleReply;
import com.chaoshan.util.api.R;
import com.chaoshan.vo.TopicArticleReplyVO;

import java.util.List;

/**
 * @author YCE
 * @description 针对表【cs_topic_article_reply(话题下的文章评论的回复表)】的数据库操作Service
 * @createDate 2022-05-17 17:34:59
 */
public interface TopicArticleReplyService extends IService<TopicArticleReply> {

    R<List<TopicArticleReplyVO>> getTopicArticleReplyVOByCommentId(Long commentId);
}
