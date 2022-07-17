package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ArticleTag;

/**
 * @DATE: 2022/05/13 14:39
 * @Author: 小爽帅到拖网速
 */

public interface ArticleTagService extends IService<ArticleTag> {


    /**
     * 根据tag “1,2,3,4” 获取标签名
     *
     * @param tag
     * @return
     */
    Object[] getTagsByIds(String tag);

    /**
     * 根据标签字符串数组返回对应标签表里的id并拼接长字符串
     *
     * @param tags
     * @return
     */
    String getIdsByTags(String[] tags);
}
