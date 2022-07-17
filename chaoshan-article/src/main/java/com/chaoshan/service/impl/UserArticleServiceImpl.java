package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserArticle;
import com.chaoshan.mapper.UserArticleMapper;
import com.chaoshan.service.UserArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@Service
public class UserArticleServiceImpl extends ServiceImpl<UserArticleMapper, UserArticle> implements UserArticleService {

    @Autowired
    private UserArticleMapper userArticleMapper;

    /**
     * 根据文章id获取文章作者信息
     *
     * @param id
     * @return
     */
    @Override
    public User getUserByArticleId(Long id) {
        return userArticleMapper.getUserByArticleId(id);
    }
}
