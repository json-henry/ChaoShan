package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ArticleCollection;
import com.chaoshan.util.api.R;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

public interface ArticleCollectionService extends IService<ArticleCollection> {


    /**
     * 用户收藏其他用户的文章
     *
     * @param articleCollection
     * @return
     */
    R collectionUserArticle(ArticleCollection articleCollection);

    /**
     * 删除文章被收藏记录
     *
     * @param articleid
     */
    void deleteRecord(String articleid);
}
