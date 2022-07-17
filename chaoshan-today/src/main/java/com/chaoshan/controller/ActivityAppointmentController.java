package com.chaoshan.controller;

import com.chaoshan.service.impl.ActivityAppointmentServiceImpl;
import com.chaoshan.util.api.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-18  22:04
 * @Description: 活动预约接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/activity/appointment")
public class ActivityAppointmentController {

    @Autowired
    private ActivityAppointmentServiceImpl service;

    @PostMapping("/{activityId}")
    @ApiOperation(value = "通过活动id进行活动预约")
    @ApiImplicitParam(name = "activityId", value = "活动id", required = true, paramType = "path")
    public R addActivityAppointment(@PathVariable("activityId") Long activityId) {
        return service.appointment(activityId);
    }

    @DeleteMapping("/{activityId}")
    @ApiOperation(value = "通过活动id取消活动预约")
    @ApiImplicitParam(name = "activityId", value = "活动id", required = true, paramType = "path")
    public R ActivityUnAppointment(@PathVariable("activityId") Long activityId) {
        return service.unAppointment(activityId);
    }


}
