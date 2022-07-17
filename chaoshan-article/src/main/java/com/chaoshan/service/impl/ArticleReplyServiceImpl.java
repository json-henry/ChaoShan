package com.chaoshan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.InformationClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.ArticleComment;
import com.chaoshan.entity.ArticleReply;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.ArticleCommentMapper;
import com.chaoshan.mapper.ArticleReplyMapper;
import com.chaoshan.service.ArticleReplyService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import com.chaoshan.util.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @DATE: 2022/05/12 09:02
 * @Author: 小爽帅到拖网速
 */

@Service
@Slf4j
public class ArticleReplyServiceImpl extends ServiceImpl<ArticleReplyMapper, ArticleReply> implements ArticleReplyService {

    @Autowired
    private ArticleReplyMapper articleReplyMapper;
    @Autowired
    private ArticleCommentMapper articleCommentMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private InformationClient informationClient;

    /**
     * 用户回复文章评论
     *
     * @param articleReply
     * @return
     */
    @Override
    public R replyCommentArticle(ArticleReply articleReply) {
        User currentLoginUser = LoginUser.getCurrentLoginUser();
        if (ObjectUtil.isNotEmpty(currentLoginUser)) {
            articleReply.setAccountid(currentLoginUser.getAccountid());
        }
        int insertCount = articleReplyMapper.insert(articleReply);
        if (insertCount == 1) {
            // 根据评论id查询文章id
            ArticleComment articleComment = articleCommentMapper.selectOne(new LambdaQueryWrapper<ArticleComment>()
                    .eq(ArticleComment::getId, articleReply.getCommentId()).select(ArticleComment::getArticleid));
            if (ObjectUtil.isEmpty(articleComment)) {
                return R.fail("该评论不存在");
            }
            Long articleid = articleComment.getArticleid();
            // 封装消息
            UserMessage userMessage = new UserMessage()
                    .setReceiveAccountid(articleReply.getToAccountid())
                    .setSendAccountid(articleReply.getAccountid())
                    .setArticleid(articleid)
                    .setMessage(articleReply.getContent())
                    .setMessageType(MessageConstant.ARTICLE_REPLY_MESSAGE);
            // rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE,ARTICLE_MESSAGE_INSERT_ROUTING,userMessage);
            informationClient.addMessage(userMessage);
            return R.success(ResultCode.SUCCESS);
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 删除评论回复
     *
     * @param commentId
     * @return
     */
    @Override
    public Long deleteRecord(Long commentId) {
        ArticleReply articleReply =
                articleReplyMapper.selectOne(new LambdaQueryWrapper<ArticleReply>().select(ArticleReply::getId)
                .eq(ArticleReply::getCommentId, commentId));
        if (ObjectUtil.isNotEmpty(articleReply)) {
            Long commentReplyId = articleReply.getId();
            // 评论回复记录存在
            if (!(articleReplyMapper.deleteById(commentReplyId) == 1)) {
                log.error("该评论回复记录id：{}删除失败", commentReplyId);
            }
            return commentReplyId;
        }
        return 0L;
    }

    /**
     * 根据评论id删除文章回复评论
     *
     * @param commentId
     * @return
     */
    @Override
    public R deleteReplyCommentArticle(Long commentId) {
        User currentLoginUser = LoginUser.getCurrentLoginUser();
        if (ObjectUtil.isEmpty(currentLoginUser)) {
            return R.fail("当前用户未登录！");
        }
        ArticleReply articleReply = articleReplyMapper.selectOne(new LambdaQueryWrapper<ArticleReply>()
                .select(ArticleReply::getAccountid)
                .eq(ArticleReply::getId, commentId));
        if (ObjectUtil.isEmpty(articleReply)) {
            return R.fail(ResultCode.FAILURE, "该评论id无效，评论不存在");
        }
        if (ObjectUtil.isNotEmpty(articleReply.getAccountid()) && !(articleReply.getAccountid().equals(currentLoginUser.getAccountid()))) {
            return R.fail("评论发起人才能够删除该评论");
        }
        if (articleReplyMapper.delete(new LambdaQueryWrapper<ArticleReply>().eq(ArticleReply::getId, commentId)) > 0) {
            return R.success(ResultCode.SUCCESS, "删除成功！");
        }
        return R.fail(ResultCode.FAILURE, "业务异常，删除失败");
    }
}
