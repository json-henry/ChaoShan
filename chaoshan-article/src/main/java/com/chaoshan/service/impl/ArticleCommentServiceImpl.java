package com.chaoshan.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.InformationClient;
import com.chaoshan.clients.UserClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.*;
import com.chaoshan.mapper.ArticleCommentMapper;
import com.chaoshan.mapper.ArticleCommentReplyStarMapper;
import com.chaoshan.mapper.ArticleCommentStarMapper;
import com.chaoshan.mapper.UserArticleMapper;
import com.chaoshan.service.ArticleCommentService;
import com.chaoshan.util.UtilMethod;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import com.chaoshan.util.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.chaoshan.util.UtilMethod.copyUserIgnoreField;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@Service
@Slf4j
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements ArticleCommentService {

    @Autowired
    private ArticleCommentMapper articleCommentMapper;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ArticleCommentStarMapper articleCommentStarMapper;
    @Autowired
    private ArticleCommentReplyStarMapper articleCommentReplyStarMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private InformationClient informationClient;
    @Autowired
    private UserArticleMapper userArticleMapper;

    /**
     * 根据文章id获取文章评论
     *
     * @param id
     * @return
     */
    @Override
    public List<ArticleComment> getCommentByArticleId(Long id) {
        List<ArticleComment> commentByArticleId = articleCommentMapper.getCommentByArticleId(id);

        // 通过流处理返回结果，通过评论和子评论中的用户账号查询用户详情并赋值后返回
        List<ArticleComment> handleResult = commentByArticleId.stream().map(articleComment -> {
            // 处理当前登录用户是否对文章评论进行点赞
            handleCurrentStarComment(articleComment);

            // 填充点赞数
            Integer articleCommentStar =
                    articleCommentStarMapper.selectCount(new LambdaQueryWrapper<ArticleCommentStar>().eq(ArticleCommentStar::getCommentId, articleComment.getId()));
            articleComment.setStar(articleCommentStar);

            // 评论中用户对象填充
            User userDb = userClient.getUserById(articleComment.getAccountid()).getData();
            User commentUser = copyUserIgnoreField(userDb);
            articleComment.setSendUser(commentUser);
            // 处理评论中的回复
            List<ArticleReply> articleReplyList = articleComment.getChildren().stream().map(articleReply -> {
                // 填充父评论id
                articleReply.setParentCommentId(articleComment.getId());

                // 回复评论的点赞数填充
                Integer commentReplyStar =
                        articleCommentReplyStarMapper.selectCount(new LambdaQueryWrapper<ArticleCommentReplyStar>().eq(ArticleCommentReplyStar::getCommentReplyId, articleReply.getId()));
                articleReply.setStar(commentReplyStar);

                // 回复评论中的对象填充
                // 如果回复的对象是父评论用户,即本条回复对象是父评论用户，则返回null
                if (articleReply.getToAccountid().equals(commentUser.getAccountid())) {
                    articleReply.setTargetUser(null);
                }
                // 处理主动回复评论的用户
                User userDbReply = userClient.getUserById(articleReply.getAccountid()).getData();
                articleReply.setReplyUser(copyUserIgnoreField(userDbReply));
                // 处理被回复用户
                User userDbReplyTarget = userClient.getUserById(articleReply.getToAccountid()).getData();
                articleReply.setTargetUser(copyUserIgnoreField(userDbReplyTarget));
                return articleReply;
            }).sorted(Comparator.comparing(ArticleReply::getCreateTime)).collect(Collectors.toList());

            articleComment.setChildren(articleReplyList);
            return articleComment;
        }).collect(Collectors.toList());

        return handleResult;
    }

    /**
     * 处理当前登录用户是否对文章评论进行点赞
     *
     * @param articleComment
     */
    private void handleCurrentStarComment(ArticleComment articleComment) {
        User currentLoginUser = LoginUser.getCurrentLoginUser();
        if (ObjectUtil.isNotEmpty(currentLoginUser)) {
            // 查询点赞记录表是否存在当前用户点赞该评论
            Integer selectCount = articleCommentStarMapper.selectCount(new LambdaQueryWrapper<ArticleCommentStar>()
                    .eq(ArticleCommentStar::getAccountid, currentLoginUser.getAccountid())
                    .eq(ArticleCommentStar::getCommentId, articleComment.getId()));
            if (selectCount > 0) {
                articleComment.setLike(true);
            }
        }
    }

    /**
     * 用户评论文章
     *
     * @param articleComment
     * @return
     */
    @Override
    public R commentArticle(ArticleComment articleComment) {
        User currentLoginUser = LoginUser.getCurrentLoginUser();
        if (ObjectUtil.isNotEmpty(currentLoginUser)) {
            articleComment.setAccountid(currentLoginUser.getAccountid());
        }
        int recordCount = articleCommentMapper.insert(articleComment);
        if (recordCount == 1) {
            // 查询文章作者账号
            String accountidByArticleId = UtilMethod.getAccountidByArticleId(articleComment.getArticleid(),
                    userArticleMapper);
            UserMessage userMessage = new UserMessage()
                    .setReceiveAccountid(accountidByArticleId)
                    .setSendAccountid(articleComment.getAccountid())
                    .setArticleid(articleComment.getArticleid())
                    .setMessage(articleComment.getMessage())
                    .setMessageType(MessageConstant.ARTICLE_COMMENT_MESSAGE);
            informationClient.addMessage(userMessage);
            // rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_MESSAGE_INSERT_ROUTING, JSONUtil.toJsonStr
            // (userMessage));
            return R.success(ResultCode.SUCCESS);
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 删除评论记录，如果评论记录存在则返回该评论id
     *
     * @param articleid
     */
    @Override
    public Long deleteRecord(String articleid) {
        ArticleComment articleComment = articleCommentMapper.selectOne(new LambdaQueryWrapper<ArticleComment>()
                .select(ArticleComment::getId)
                .eq(ArticleComment::getArticleid, articleid));
        if (ObjectUtil.isNotEmpty(articleComment)) {
            Long commentId = articleComment.getId();
            if (!(articleCommentMapper.deleteById(commentId) == 1)) {
                log.error("删除文章评论id：{}失败", commentId);
            }
            return commentId;
        }
        return 0L;
    }

    /**
     * 根据评论id删除文章评论
     *
     * @param commentId
     * @return
     */
    @Override
    public R deleteCommentArticle(Long commentId) {
        User currentLoginUser = LoginUser.getCurrentLoginUser();
        if (ObjectUtil.isEmpty(currentLoginUser)) {
            return R.fail("当前用户未登录！");
        }
        ArticleComment articleCommentDb = articleCommentMapper.selectOne(new LambdaQueryWrapper<ArticleComment>().select(ArticleComment::getAccountid)
                .eq(ArticleComment::getId, commentId));
        if (ObjectUtil.isEmpty(articleCommentDb)) {
            return R.fail(ResultCode.FAILURE, "该评论id无效，评论不存在");
        }
        if (!(articleCommentDb.getAccountid().equals(currentLoginUser.getAccountid()))) {
            return R.fail("评论发起人才能够删除该评论");
        }

        if (articleCommentMapper.delete(new LambdaQueryWrapper<ArticleComment>().eq(ArticleComment::getId, commentId)) > 0) {
            return R.success(ResultCode.SUCCESS, "删除成功！");
        }
        return R.fail(ResultCode.FAILURE, "业务异常，删除失败");
    }


}
