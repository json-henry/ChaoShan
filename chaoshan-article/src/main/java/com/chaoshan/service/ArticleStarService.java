package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ArticleStar;
import com.chaoshan.util.api.R;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

public interface ArticleStarService extends IService<ArticleStar> {

    /**
     * 用户对文章点赞操作，根据请求会去判断当前是否点赞，取反操作
     *
     * @param articleStar
     * @return
     */
    R starArticleOper(ArticleStar articleStar);
}
