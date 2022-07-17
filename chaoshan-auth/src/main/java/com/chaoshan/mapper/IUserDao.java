package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.Permission;
import com.chaoshan.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @DATE: 2022/04/28 14:37
 * @Author: 小爽帅到拖网速
 */
@Mapper
public interface IUserDao extends BaseMapper<User> {
    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    List<Permission> findUserPermission(Long userId);

    /**
     * 用户授权
     */
    int grantPermission(Long userId, String roleId);
}
