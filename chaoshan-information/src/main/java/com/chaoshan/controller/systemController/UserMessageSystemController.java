package com.chaoshan.controller.systemController;


import com.chaoshan.clients.UserClient;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.service.impl.UserMessageServiceImpl;
import com.chaoshan.util.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hyx
 * @since 2022-05-12
 */
@RestController
@RequestMapping("/system/user-message")
@Api("管理员操作消息相关接口")
public class UserMessageSystemController {

    @Autowired
    private UserMessageServiceImpl messageService;

    @Autowired
    private UserClient userClient;

    @PostMapping("/add")
    @ApiIgnore
    @ApiOperation("新增单条消息记录")
    public R addMessage(@RequestBody UserMessage userMessage) {
        if (messageService.save(userMessage)) {
            return R.success("发送消息成功");
        }
        return R.fail("发送消息失败");
    }

    @PostMapping("/add/allUser")
    @ApiIgnore
    @ApiOperation("给所有用户新增消息记录")
    public R addMessageToAllUser(Integer messageType) {
        List<String> allAccountIds = userClient.getAllAccountIds().getData();
        List<UserMessage> userMessages = new ArrayList<>();
        for (String accountId : allAccountIds) {
            userMessages.add(new UserMessage()
                    .setReceiveAccountid(accountId)
                    .setSendAccountid("admin")
                    .setMessageType(messageType));
        }
        if (messageService.saveBatch(userMessages)) {
            return R.success("发送消息成功！");
        }
        return R.fail("发送消息失败！");
    }


}
