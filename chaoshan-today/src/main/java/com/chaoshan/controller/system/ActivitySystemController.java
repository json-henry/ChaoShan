package com.chaoshan.controller.system;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.chaoshan.clients.UserClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.Activity;
import com.chaoshan.service.impl.ActivityServiceImpl;
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
 * @CreateTime: 2022-05-22  12:25
 * @Description: 后台操作活动相关接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/system/activity")
public class ActivitySystemController {

    @Autowired
    private ActivityServiceImpl service;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserClient userClient;

    @PostMapping("/add")
    @ApiOperation(value = "后台管理员新增活动")
    @ApiIgnore
    public R addActivity(@RequestBody Activity activity) {
        if (ObjectUtils.isEmpty(activity)) {
            return R.fail("添加失败，新增活动不能为空");
        }
        ChineseDate date = new ChineseDate(DateUtil.parseDate(activity.getCalendar().toString()));
        activity.setLunar(date.getChineseYear() + "年" + date.getChineseMonth() + date.getChineseDay());
        if (service.save(activity)) {
            Map<String, Object> map = new HashMap<>();
            map.put("accountId", LoginUser.getCurrentLoginUser().getAccountid());
            map.put("articleId", activity.getId());
            map.put("type", MessageConstant.ACTIVITY_MESSAGE);
            map.put("allAccount", userClient.getAllAccountIds().getData());
            //给所有的用户发送消息
            rabbitTemplate.convertAndSend(MESSAGE_EXCHANGE, MESSAGE_INSERT_ALL_KEY, JSONUtil.toJsonStr(map));
            //将数据同步到es中
            rabbitTemplate.convertAndSend(ACTIVITY_EXCHANGE, ACTIVITY_INSERT_KEY, activity.getId());
            return R.success("添加活动成功");
        }
        return R.fail("添加活动失败");
    }

    @DeleteMapping("/{activityId}")
    @ApiOperation("管理员删除活动")
    public R deleteActivity(@PathVariable Long activityId) {
        if (ObjectUtils.isEmpty(activityId)) {
            return R.fail("删除失败");
        }
        if (service.removeById(activityId)) {
            rabbitTemplate.convertAndSend(ACTIVITY_EXCHANGE, ACTIVITY_DELETE_KEY, activityId);
            return R.success("删除成功");
        }

        return R.fail("删除失败");
    }
}
