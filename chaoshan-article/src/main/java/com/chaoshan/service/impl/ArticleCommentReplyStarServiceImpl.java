package com.chaoshan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.InformationClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.ArticleCommentReplyStar;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.ArticleCommentReplyStarMapper;
import com.chaoshan.mapper.ArticleReplyMapper;
import com.chaoshan.service.ArticleCommentReplyStarService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @DATE: 2022/05/14 08:33
 * @Author: 小爽帅到拖网速
 */

@Service
@Slf4j
public class ArticleCommentReplyStarServiceImpl extends ServiceImpl<ArticleCommentReplyStarMapper,
        ArticleCommentReplyStar> implements ArticleCommentReplyStarService {

    @Autowired
    private ArticleCommentReplyStarMapper articleCommentReplyStarMapper;
    @Autowired
    private ArticleReplyMapper articleReplyMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private InformationClient informationClient;

    /**
     * 用户对文章评论回复的点赞操作，根据请求会判断当前是否点赞，取反操作
     *
     * @param articleCommentReplyStar
     * @return
     */
    @Override
    public R starArticleCommentReplyOper(ArticleCommentReplyStar articleCommentReplyStar) {
        if (ObjectUtil.hasEmpty(articleCommentReplyStar.getAccountid(), articleCommentReplyStar.getCommentReplyId())) {
            return R.fail(ResultCode.PARAM_MISS, "账号id或评论id不能为空");
        }
        // 先判断是否有记录，如果有则为取消点赞操作，如果没有则为点赞操作
        Integer count = articleCommentReplyStarMapper.selectCount(new LambdaQueryWrapper<ArticleCommentReplyStar>()
                .eq(ArticleCommentReplyStar::getAccountid, articleCommentReplyStar.getAccountid()).eq(ArticleCommentReplyStar::getCommentReplyId, articleCommentReplyStar.getCommentReplyId()));
        if (count > 0) {
            // 用户取消点赞操作
            int changeCount = articleCommentReplyStarMapper.delete(new LambdaQueryWrapper<ArticleCommentReplyStar>()
                    .eq(ArticleCommentReplyStar::getAccountid, articleCommentReplyStar.getAccountid()).eq(ArticleCommentReplyStar::getCommentReplyId, articleCommentReplyStar.getCommentReplyId()));
            if (changeCount < 0) {
                return R.fail(ResultCode.FAILURE, "用户取消点赞操作失败");
            }
        } else {
            // 用户点赞操作
            int insert = articleCommentReplyStarMapper.insert(articleCommentReplyStar);
            if (!(insert == 1)) {
                return R.fail(ResultCode.FAILURE, "用户点赞操作失败");
            }

            // 根据文章评论回复id查询文章id
            Long articleid = articleReplyMapper.getArticleByReplyId(articleCommentReplyStar.getCommentReplyId());
            UserMessage userMessage =
                    new UserMessage().setArticleid(articleid).setSendAccountid(articleCommentReplyStar.getAccountid())
                    .setMessageType(MessageConstant.ARTICLE_STAR_COMMENT_MESSAGE);

            // rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE,ARTICLE_MESSAGE_INSERT_ROUTING, JSONUtil.toJsonStr
            // (userMessage));
            informationClient.addMessage(userMessage);
        }
        return R.success(ResultCode.SUCCESS);
    }

    /**
     * 删除评论回复点赞记录
     *
     * @param commentReplyId
     */
    @Override
    public void deleteRecord(Long commentReplyId) {
        ArticleCommentReplyStar articleCommentReplyStar =
                articleCommentReplyStarMapper.selectOne(new LambdaQueryWrapper<ArticleCommentReplyStar>()
                .select(ArticleCommentReplyStar::getCommentReplyId)
                .eq(ArticleCommentReplyStar::getCommentReplyId, commentReplyId));
        if (ObjectUtil.isNotEmpty(articleCommentReplyStar)) {
            Long commentReplyStarId = articleCommentReplyStar.getId();
            if (!(articleCommentReplyStarMapper.deleteById(commentReplyStarId) == 1)) {
                log.error("删除文章回复点赞记录id：{}失败", commentReplyId);
            }
        }


    }
}
