package com.chaoshan.controller.system;

import cn.hutool.json.JSONUtil;
import com.chaoshan.clients.UserClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.Topic;
import com.chaoshan.service.impl.TopicServiceImpl;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

import static com.chaoshan.constant.MessageConstant.MESSAGE_EXCHANGE;
import static com.chaoshan.constant.MessageConstant.MESSAGE_INSERT_ALL_KEY;
import static com.chaoshan.constant.RabbitMQConstant.*;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-18  22:13
 * @Description: 后台操作话题相关接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/system/topic")
public class TopicSystemController {

    @Autowired
    private TopicServiceImpl service;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserClient userClient;

    @PostMapping("/publish")
    @ApiIgnore
    @ApiOperation(value = "管理员发布话题")
    public R publishTopic(@RequestBody Topic topic) {
        if (ObjectUtils.isEmpty(topic)) {
            return R.fail("发布内容不能为空");
        }
        if (service.save(topic)) {
            Map<String, Object> map = new HashMap<>();
            map.put("accountId", LoginUser.getCurrentLoginUser().getAccountid());
            map.put("articleId", topic.getId());
            map.put("type", MessageConstant.TOPIC_MESSAGE);
            map.put("allAccount", userClient.getAllAccountIds().getData());
            //给所有的用户发送消息
            rabbitTemplate.convertAndSend(MESSAGE_EXCHANGE, MESSAGE_INSERT_ALL_KEY, JSONUtil.toJsonStr(map));

            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, TOPIC_INSERT_KEY, topic.getId());
            return R.success("发布成功");
        }
        return R.fail("发布失败");
    }

    @DeleteMapping("/{topicId}")
    @ApiOperation("管理员删除话题")
    public R deleteTopic(@PathVariable Long topicId) {
        if (ObjectUtils.isEmpty(topicId)) {
            return R.fail("删除失败");
        }
        if (service.removeById(topicId)) {
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, TOPIC_DELETE_KEY, topicId);
            return R.success("删除成功");
        }
        return R.fail("删除失败");
    }

}
