package com.chaoshan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.InformationClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.ArticleComment;
import com.chaoshan.entity.ArticleCommentStar;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.ArticleCommentMapper;
import com.chaoshan.mapper.ArticleCommentStarMapper;
import com.chaoshan.mapper.UserArticleMapper;
import com.chaoshan.service.ArticleCommentStarService;
import com.chaoshan.util.UtilMethod;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import com.chaoshan.util.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @DATE: 2022/05/13 14:39
 * @Author: 小爽帅到拖网速
 */

@Service
@Slf4j
public class ArticleCommentStarServiceImpl extends ServiceImpl<ArticleCommentStarMapper, ArticleCommentStar> implements ArticleCommentStarService {

    @Autowired
    private ArticleCommentStarMapper articleCommentStarMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private InformationClient informationClient;
    @Autowired
    private ArticleCommentMapper articleCommentMapper;
    @Autowired
    private UserArticleMapper userArticleMapper;

    /**
     * 用户对文章评论的点赞操作，根据请求会去判断当前是否点赞，取反操作
     *
     * @param commentId
     * @return
     */
    @Override
    public R starArticleCommentOper(Long commentId) {

        if (ObjectUtil.hasEmpty(commentId)) {
            return R.fail(ResultCode.PARAM_MISS, "评论id不能为空");
        }
        User currentLoginUser = LoginUser.getCurrentLoginUser();
        if (ObjectUtil.isEmpty(currentLoginUser)) {
            return R.fail(ResultCode.PARAM_MISS, "当前用户未登录");
        }
        ArticleCommentStar articleCommentStar = new ArticleCommentStar();
        articleCommentStar.setAccountid(currentLoginUser.getAccountid());
        articleCommentStar.setCommentId(commentId);
        // 根据评论id 找到文章id
        ArticleComment articleComment = articleCommentMapper.selectOne(new LambdaQueryWrapper<ArticleComment>()
                .select(ArticleComment::getArticleid).eq(ArticleComment::getId, commentId));
        if (ObjectUtil.isNotEmpty(articleComment)) {
            articleCommentStar.setArticleid(articleComment.getArticleid());
        }

        // 先判断是否有记录，如果有则为取消点赞操作，如果没有则为点赞操作
        Integer count = articleCommentStarMapper.selectCount(new LambdaQueryWrapper<ArticleCommentStar>()
                .eq(ArticleCommentStar::getAccountid, articleCommentStar.getAccountid())
                .eq(ArticleCommentStar::getCommentId, articleCommentStar.getCommentId()));
        if (count > 0) {
            // 用户取消点赞操作
            int changeCount = articleCommentStarMapper.delete(new LambdaQueryWrapper<ArticleCommentStar>()
                    .eq(ArticleCommentStar::getAccountid, articleCommentStar.getAccountid()).eq(ArticleCommentStar::getCommentId, articleCommentStar.getCommentId()));
            if (changeCount == 0) {
                return R.fail(ResultCode.FAILURE, "用户取消点赞操作失败");
            }
        } else {
            // 用户点赞操作
            if (articleCommentStarMapper.insert(articleCommentStar) < 0) {
                return R.fail(ResultCode.FAILURE, "用户点赞操作失败");
            }
            // 如果24小时内只有1条相同的记录则插入消息表
            int countRecently = articleCommentStarMapper.selectRecently(articleCommentStar.getAccountid(),
                    articleCommentStar.getArticleid(), articleCommentStar.getCommentId());
            if (countRecently == 1) {
                log.info("插入消息");
                // 查询文章作者
                String accountidByArticleId = UtilMethod.getAccountidByArticleId(articleCommentStar.getArticleid(),
                        userArticleMapper);
                // 发送消息
                UserMessage userMessage = new UserMessage().setArticleid(articleCommentStar.getArticleid())
                        .setReceiveAccountid(accountidByArticleId)
                        .setSendAccountid(articleCommentStar.getAccountid())
                        .setMessageType(MessageConstant.ARTICLE_STAR_COMMENT_MESSAGE);
                try {
                    informationClient.addMessage(userMessage);
                } catch (Exception e) {
                    log.error("用户点赞消息发送失败");
                }
                // rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE,ARTICLE_MESSAGE_INSERT_ROUTING, JSONUtil.toJsonStr(userMessage));
            }

        }
        return R.success(ResultCode.SUCCESS);
    }
}
