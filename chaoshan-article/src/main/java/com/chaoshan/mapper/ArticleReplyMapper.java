package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.ArticleReply;
import org.apache.ibatis.annotations.Mapper;

/**
 * @DATE: 2022/05/12 09:02
 * @Author: 小爽帅到拖网速
 */

@Mapper
public interface ArticleReplyMapper extends BaseMapper<ArticleReply> {
    /**
     * 根据文章评论回复id查询文章id
     *
     * @param commentReplyId
     * @return
     */
    Long getArticleByReplyId(Long commentReplyId);
}