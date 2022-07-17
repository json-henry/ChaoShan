package com.chaoshan.service.impl;

import cn.hutool.json.JSONUtil;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-13  15:11
 * @Description:
 * @Version: 1.0
 */
@SpringBootTest
@Slf4j
class UserMessageServiceImplTest {

    @Autowired
    private UserMessageServiceImpl service;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void deleteMessage() {
        service.removeById(2L);
    }

    @Test
    void getMessageNumberMapByType() {
        Map<String, Long> messageNumberMapByType = service.getMessageNumberMapByType(2, 3, 4);
        String s = JSONUtil.toJsonStr(messageNumberMapByType);
        System.out.println(s);
    }

    @Test
    void sendMessageToMQ() {
        List<UserMessage> userMessages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            userMessages.add(new UserMessage().setMessage(i + ""));
        }
        rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE, MessageConstant.MESSAGE_INSERT_ALL_KEY,
                userMessages);
    }
}