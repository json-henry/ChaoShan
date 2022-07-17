package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.Permission;
import com.chaoshan.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @DATE: 2022/04/28 14:40
 * @Author: 小爽帅到拖网速
 */

public interface IUserService extends IService<User> {

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    List<Permission> findUserPermission(Long userId);

    /**
     * 首次登录即注册
     */
    User registerUserOfLogin(String username, PasswordEncoder passwordEncoder);

}
