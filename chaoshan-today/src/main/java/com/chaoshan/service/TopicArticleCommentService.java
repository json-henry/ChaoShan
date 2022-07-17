package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.TopicArticleComment;
import com.chaoshan.vo.TopicArticleCommentVO;

import java.util.List;

/**
 * @author YCE
 * @description 针对表【cs_topic_article_comment(话题文章评论表)】的数据库操作Service
 * @createDate 2022-05-17 17:34:59
 */
public interface TopicArticleCommentService extends IService<TopicArticleComment> {

    /**
     * 根据话题文章id获取文章的所有评论，并携带每条评论的点赞数和回复数
     *
     * @param articleId
     * @return
     */
    List<TopicArticleCommentVO> getArticleCommentVOList(Long articleId);

}
