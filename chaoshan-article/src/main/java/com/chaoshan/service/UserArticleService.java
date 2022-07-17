package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserArticle;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

public interface UserArticleService extends IService<UserArticle> {


    /**
     * 根据文章id获取文章作者信息
     *
     * @param id
     * @return
     */
    User getUserByArticleId(Long id);
}
