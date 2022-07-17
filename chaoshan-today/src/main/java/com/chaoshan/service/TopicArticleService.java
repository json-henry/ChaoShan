package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.TopicArticle;
import com.chaoshan.util.api.R;
import com.chaoshan.vo.TopicArticleVO;

/**
 * @author YCE
 * @description 针对表【cs_topic_article(活动文章表)】的数据库操作Service
 * @createDate 2022-05-17 17:34:59
 */
public interface TopicArticleService extends IService<TopicArticle> {
    /**
     * 话题文章的点赞与取消点赞操作
     *
     * @param articleId
     * @return
     */
    R starTopicArticle(Long articleId);

    /**
     * 根据文章id获取文章Vo，即提供给前端接收的对象
     *
     * @param articleId
     * @return
     */
    TopicArticleVO getTopicArticleVOById(Long articleId);
}
