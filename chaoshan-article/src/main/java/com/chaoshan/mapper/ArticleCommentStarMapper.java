package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.ArticleCommentStar;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @DATE: 2022/05/13 14:39
 * @Author: 小爽帅到拖网速
 */

@Mapper
public interface ArticleCommentStarMapper extends BaseMapper<ArticleCommentStar> {
    /**
     * 根据3个字段查询24小时内是否有相同的记录
     *
     * @param accountid
     * @param articleid
     * @param commentId
     * @return
     */
    int selectRecently(@Param("accountid") String accountid, @Param("articleid") Long articleid, @Param("commentId") Long commentId);
}