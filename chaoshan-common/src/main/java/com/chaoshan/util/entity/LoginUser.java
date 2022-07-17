package com.chaoshan.util.entity;

import cn.hutool.json.JSONUtil;
import com.chaoshan.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * 获取当前用户登录信息
 *
 * @DATE: 2022/05/12 10:48
 * @Author: 小爽帅到拖网速
 */
public class LoginUser {
    public static User getCurrentLoginUser() {
        User user = null;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            user = JSONUtil.toBean(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(),
                    User.class);
        } finally {
            return user;
        }
    }
}
