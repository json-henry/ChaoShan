package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.User;
import com.chaoshan.entity.dto.UserDTO;
import com.chaoshan.entity.dto.UserPasswordDTO;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.Rpage;

import java.util.List;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */
public interface UserService extends IService<User> {

    /**
     * 获取用户分页数据
     *
     * @param currentPage 当前页
     * @param size        数量
     * @return
     */
    R<Rpage> getUserByPage(Integer currentPage, Integer size);

    /**
     * 获取用户信息
     * 返回用户名、头像、简介及关注数、粉丝数、所有收藏和点赞数及邀请用户数
     */
    R<UserDTO> getUserInfo(String accountid);

    /**
     * 获取被邀请用户列表
     */
    List<User> getInvitedUser(String accountid);

    /**
     * 获取所有用户的账号信息
     */
    List<String> getAllAccountId();

    /**
     * 修改用户信息
     */
    R updateUserInfo(User user);

    /**
     * 当前登录用户修改密码
     *
     * @param userPasswordDTO
     * @return
     */
    R modifyPassword(UserPasswordDTO userPasswordDTO);
}