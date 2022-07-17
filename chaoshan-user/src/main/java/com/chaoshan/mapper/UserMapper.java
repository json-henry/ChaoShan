package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chaoshan.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 呱呱
 * @date Created in 2022/5/15 17:51
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 分页获取用户数据
     *
     * @param page
     * @return
     */
    IPage<User> getUserByPage(Page<User> page);

    /***
     * 获取所有用户
     */
    List<User> selectAll();
}