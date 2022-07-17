package com.chaoshan.controller;

import cn.hutool.core.util.StrUtil;
import com.chaoshan.clients.UserClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.TopicArticleComment;
import com.chaoshan.entity.TopicArticleReply;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.TopicArticleCommentMapper;
import com.chaoshan.service.impl.TopicArticleReplyServiceImpl;
import com.chaoshan.service.impl.TopicArticleReplyStarServiceImpl;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import com.chaoshan.vo.TopicArticleReplyVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-21  12:38
 * @Description: 话题文章回复相关接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/topic/articleReply")
public class TopicArticleReplyController {

    @Autowired
    private TopicArticleReplyServiceImpl replyService;
    @Autowired
    private TopicArticleReplyStarServiceImpl starService;
    @Autowired
    private TopicArticleCommentMapper commentMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserClient userClient;

    @GetMapping("/{commentId}")
    @ApiOperation(value = "获取话题文章评论的回复", notes = "开放接口，根据话题文章评论的id，来获取对应的评论")
    @ApiImplicitParam(name = "commentId", value = "评论id", required = true, paramType = "path")
    public R<List<TopicArticleReplyVO>> getReplyByCommentId(@PathVariable("commentId") Long commentId) {
        return replyService.getTopicArticleReplyVOByCommentId(commentId);
    }

    @PostMapping("/star/{replyId}")
    @ApiOperation(value = "点赞回复")
    @ApiImplicitParam(name = "replyId", value = "回复id", required = true, paramType = "path")
    public R starReply(@PathVariable("replyId") Long replyId) {
        return starService.starReply(replyId);
    }

    @PostMapping("/reply")
    @ApiOperation(value = "回复评论")
    public R reply(@RequestBody TopicArticleReply reply) {
        if (ObjectUtils.isEmpty(reply)) {
            return R.fail("回复失败，内容不能为空");
        }
        TopicArticleComment comment = commentMapper.selectById(reply.getArticleCommentId());
        if (ObjectUtils.isEmpty(comment)) {
            return R.fail("不存在该评论");
        }
        Long articleId = comment.getArticleId();
        String receiveAccountId = comment.getSendAccoutid();
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        if (StrUtil.isEmpty(reply.getAccountid())) {
            reply.setAccountid(accountId);
        }
        reply.setToAccountid(receiveAccountId);
        //评论成功后且评论的对象不是自己的情况下就发送消息
        if (replyService.save(reply) && !accountId.equals(receiveAccountId)) {
            //封装消息对象
            UserMessage message = new UserMessage(articleId, accountId, receiveAccountId, reply.getContent(),
                    MessageConstant.TOPIC_REPLY_MESSAGE);
            rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE, MessageConstant.MESSAGE_INSERT_KEY,
                    message);
            return R.success("回复成功");
        }
        return R.fail("回复失败");
    }
}
