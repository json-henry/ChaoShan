package com.chaoshan.mq;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.service.impl.UserMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-22  14:46
 * @Description: 消息相关监听器
 * @Version: 1.0
 */
@Component
@Slf4j
public class MessageMqListener {

    @Autowired
    private UserMessageServiceImpl service;

    /**
     * 发送消息给某个用户
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MessageConstant.MESSAGE_INSERT_QUEUE),
            exchange = @Exchange(name = MessageConstant.MESSAGE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MessageConstant.MESSAGE_INSERT_KEY
    ))
    public void listenerMessageInsert(UserMessage message) {
        if (ObjectUtil.isEmpty(message)) {
            return;
        }
        service.save(message);
    }

    /**
     * 发送消息给所有的用户
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MessageConstant.MESSAGE_INSERT_ALL_QUEUE),
            exchange = @Exchange(name = MessageConstant.MESSAGE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MessageConstant.MESSAGE_INSERT_ALL_KEY
    ))
    public void listenerAllMessageInsert(String message) {
        if (StrUtil.isEmpty(message)) {
            return;
        }
        JSONObject jsonObject = JSONUtil.parseObj(message);
        String accountId = jsonObject.getStr("accountId");
        Long articleId = Long.valueOf(jsonObject.getStr("articleId"));
        int type = (int) jsonObject.get("type");
        List<String> accountIds = JSONUtil.toList(jsonObject.getStr("allAccount"), String.class);
        List<UserMessage> userMessages = new ArrayList<>();
        for (String id : accountIds) {
            userMessages.add(new UserMessage(articleId, accountId, id, StrUtil.EMPTY, type));
        }
        service.saveBatch(userMessages);
    }

    /**
     * 设置消息已读
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MessageConstant.MESSAGE_READ_QUEUE),
            exchange = @Exchange(name = MessageConstant.MESSAGE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MessageConstant.MESSAGE_READ_KEY
    ))
    public void listenerReadMessageByType(String message) {
        if (StrUtil.isEmpty(message)) {
            return;
        }
        JSONObject jsonObject = JSONUtil.parseObj(message);
        String accountId = jsonObject.getStr("accountId");
        int type = (int) jsonObject.get("type");
        service.update(new UserMessage().setIsRead(true),
                new LambdaQueryWrapper<UserMessage>()
                        .eq(UserMessage::getReceiveAccountid, accountId)
                        .eq(UserMessage::getMessageType, type)
        );
    }


}
