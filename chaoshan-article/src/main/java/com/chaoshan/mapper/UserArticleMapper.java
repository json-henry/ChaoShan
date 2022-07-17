package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@Mapper
public interface UserArticleMapper extends BaseMapper<UserArticle> {
    /**
     * 根据文章id查询文章作者信息
     *
     * @param id
     * @return
     */
    User getUserByArticleId(@Param("id") Long id);
}