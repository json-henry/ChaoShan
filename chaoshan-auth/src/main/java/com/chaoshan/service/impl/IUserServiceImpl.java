package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.Permission;
import com.chaoshan.entity.User;
import com.chaoshan.mapper.IUserDao;
import com.chaoshan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chaoshan.commons.RolePermissionConstants.ADMINISTRATOR_role;

/**
 * @DATE: 2022/04/28 14:41
 * @Author: 小爽帅到拖网速
 */
@Service
public class IUserServiceImpl extends ServiceImpl<IUserDao, User> implements IUserService {

    static final String INITAVATAR = "https://test-fengyue.oss-cn-guangzhou.aliyuncs" +
            ".com/images/chaoshan/32a57782d3b840789a44d727912c292b.png";

    @Autowired
    private IUserDao userDao;

    @Override
    public List<Permission> findUserPermission(Long userId) {

        return userDao.findUserPermission(userId);
    }

    /**
     * 首次登录即注册
     *
     * @param accoutid
     * @return
     */
    @Override
    public User registerUserOfLogin(String accoutid, PasswordEncoder passwordEncoder) {
        User registerUser = new User().setAccountid(accoutid)
                .setPassword(passwordEncoder.encode("123456")).setAvatar(INITAVATAR);
        userDao.insert(registerUser);
        // 查询用户并且授予权限
        User userDb = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccountid, accoutid));
        if (userDao.grantPermission(userDb.getId(), ADMINISTRATOR_role) < 0) {
            throw new RuntimeException("授予权限失败");
        }
        return userDb;
    }
}
