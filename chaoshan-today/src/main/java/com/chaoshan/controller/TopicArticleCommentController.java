package com.chaoshan.controller;

import cn.hutool.core.util.StrUtil;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.TopicArticle;
import com.chaoshan.entity.TopicArticleComment;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.TopicArticleMapper;
import com.chaoshan.service.impl.TopicArticleCommentServiceImpl;
import com.chaoshan.service.impl.TopicArticleCommentStarServiceImpl;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-21  00:10
 * @Description: 话题文章评论相关接口
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/topic/articleComment")
public class TopicArticleCommentController {

    @Autowired
    private TopicArticleCommentStarServiceImpl starService;
    @Autowired
    private TopicArticleCommentServiceImpl commentService;
    @Autowired
    private TopicArticleMapper articleMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/star/{commentId}")
    @ApiOperation(value = "进行话题文章评论的点赞")
    @ApiImplicitParam(name = "commentId", value = "评论id", required = true, paramType = "path")
    public R starComment(@PathVariable("commentId") Long commentId) {
        return starService.starComment(commentId);
    }

    @PostMapping("/comment")
    @ApiOperation(value = "进行话题文章的评论")
    public R comment(@RequestBody TopicArticleComment comment) {
        if (ObjectUtils.isEmpty(comment)) {
            return R.fail("评论失败");
        }
        if (StrUtil.isEmpty(comment.getSendAccoutid())) {
            comment.setSendAccoutid(LoginUser.getCurrentLoginUser().getAccountid());
        }
        Long articleId = comment.getArticleId();
        TopicArticle topicArticle = articleMapper.selectById(articleId);
        if (ObjectUtils.isEmpty(topicArticle)) {
            return R.fail("评论的话题文章不存在");
        }
        if (commentService.save(comment)) {
            String accountId = LoginUser.getCurrentLoginUser().getAccountid();
            String receiveAccountId = topicArticle.getAccountid();
            if (!accountId.equals(receiveAccountId)) {
                //封装消息对象
                UserMessage message = new UserMessage(articleId, accountId, receiveAccountId, comment.getMessage(),
                        MessageConstant.TOPIC_COMMENT_MESSAGE);
                rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE, MessageConstant.MESSAGE_INSERT_KEY,
                        message);
            }
            return R.success("评论成功");
        }
        return R.fail("评论失败");
    }

}
