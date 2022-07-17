package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.Topic;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author YCE
 * @description 针对表【cs_topic(话题表)】的数据库操作Service
 * @createDate 2022-05-17 17:34:59
 */
public interface TopicService extends IService<Topic> {
    /**
     * 根据话题id获取 话题和话题文章的相关内容
     */
    @Transactional
    public Map<String, Object> getTopicAndArticleById(Long id);

    /**
     * 获取热度排行靠前几的话题
     */
    List<Topic> getTopicListByHotSort(int size);

    /**
     * 根据关键字搜索话题列表
     *
     * @param searchKey
     * @return
     */
    List<Topic> getTopicListBySearchKey(String searchKey);
}
