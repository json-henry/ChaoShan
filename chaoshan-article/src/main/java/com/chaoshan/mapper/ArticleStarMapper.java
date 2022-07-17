package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.ArticleStar;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@Mapper
public interface ArticleStarMapper extends BaseMapper<ArticleStar> {
    /**
     * 通过文章id跟账号id判断是否在24小时之内是否有相同记录
     *
     * @param articleid
     * @param accountid
     * @return
     */
    int selectRecently(@Param("articleid") Long articleid, @Param("accountid") String accountid);
}