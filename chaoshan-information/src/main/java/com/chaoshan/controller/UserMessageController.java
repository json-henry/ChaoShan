package com.chaoshan.controller;


import cn.hutool.json.JSONUtil;
import com.chaoshan.clients.TodayClient;
import com.chaoshan.clients.UserClient;
import com.chaoshan.entity.*;
import com.chaoshan.service.impl.UserMessageServiceImpl;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chaoshan.constant.MessageConstant.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Hyx
 * @since 2022-05-12
 */
@RestController
@RequestMapping("/api/user-message")
@Api("用户消息相关接口")
public class UserMessageController {

    @Autowired
    private UserMessageServiceImpl messageService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private TodayClient todayClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/push/MessageNum")
    @ApiOperation("获取“系统推送”消息未读数")
    public R<Map<String, Long>> getPushMessageNumber() {
        return R.data(messageService.getMessageNumberMapByType(ACTIVITY_MESSAGE, TOPIC_MESSAGE));
    }

    @GetMapping("/aboutMe/MessageNum")
    @ApiOperation("获取 “关于我的” 消息未读数")
    public R<Map<String, Long>> getAboutMeMessageNumber() {
        return R.data(messageService.getMessageNumberMapByType(ARTICLE_COMMENT_MESSAGE, ARTICLE_REPLY_MESSAGE,
                ARTICLE_STAR_MESSAGE, TOPIC_COMMENT_MESSAGE, TOPIC_REPLY_MESSAGE, TOPIC_STAR_MESSAGE,
                ARTICLE_COLLECTION_MESSAGE, ARTICLE_STAR_COMMENT_MESSAGE, TOPIC_STAR_COMMENT_MESSAGE, FOCUS_ACCOUNTID));
    }

    @GetMapping("/push/activity/list")
    @ApiOperation("获取活动列表")
    public R<List<Activity>> getActivityList() {
        List<Activity> allActivity = todayClient.getAllActivity().getData();
        //将消息设置已读
        if (!CollectionUtils.isEmpty(allActivity)) {
            sendReadMessage(ACTIVITY_MESSAGE);
        }
        return R.data(allActivity);
    }

    @GetMapping("/push/topic/list")
    @ApiOperation("获取话题列表")
    public R<List<Topic>> getTopicList() {
        List<Topic> allTopics = todayClient.getAllTopics().getData();
        //将消息设置已读
        if (!CollectionUtils.isEmpty(allTopics)) {
            sendReadMessage(TOPIC_MESSAGE);
        }
        return R.data(allTopics);
    }

    private void sendReadMessage(int type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountId", LoginUser.getCurrentLoginUser().getAccountid());
        map.put("type", type);
        rabbitTemplate.convertAndSend(MESSAGE_EXCHANGE, MESSAGE_READ_KEY, JSONUtil.toJsonStr(map));
    }

    @GetMapping("/about-me/star-collection/list")
    @ApiOperation("获取点赞与收藏消息列表")
    public R<List<MessageDTO>> getStarAndCollectionList() {
        String accountid = LoginUser.getCurrentLoginUser().getAccountid();
        return messageService.getStarCollList(accountid);
    }

    @GetMapping("/about-me/comment-reply/list")
    @ApiOperation("获取评论和回复列表")
    public R<List<MessageDTO>> getCommentAndRelyList() {
        String accountid = LoginUser.getCurrentLoginUser().getAccountid();
        return messageService.getCommentList(accountid);
    }

    @GetMapping("/about-me/fans/list")
    @ApiOperation("获取粉丝列表")
    public R<List<User>> getFansList() {
        R<List<User>> allFans = userClient.getAllFans();
        return R.data(allFans.getData());
    }

    @PostMapping("/add")
    @ApiOperation("新增单条消息记录")
    @ApiIgnore
    public R addMessage(@RequestBody UserMessage userMessage) {
        if (messageService.save(userMessage)) {
            return R.success("发送消息成功");
        }
        return R.fail("发送消息失败");
    }


}
