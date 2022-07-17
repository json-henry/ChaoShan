package com.chaoshan.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.Activity;
import com.chaoshan.service.impl.ActivityServiceImpl;
import com.chaoshan.util.api.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-18  16:45
 * @Description: 活动相关的接口
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityServiceImpl service;

    @GetMapping("/morePage")
    @ApiOperation(value = "获取活动更多页", notes = "开放接口")
    public R<List<Activity>> getActivityMorePage() {
        return R.data(service.getAllActivity());
    }

    @GetMapping("/morePage/search/")
    @ApiOperation(value = "搜索活动相关内容", notes = "开放接口")
    @ApiImplicitParam(name = "searchKey", value = "搜索关键字", required = false, paramType = "query")
    public R<List<Activity>> getActivityListBySearchKey(@RequestParam(value = "searchKey", required = false) String searchKey) {
        if (StrUtil.isEmpty(searchKey)) {
            return getAllActivity();
        }
        return R.data(service.getActivityListBySearchKey(searchKey));
    }

    @GetMapping("/all")
    @ApiOperation(value = "获取所有的活动，并按时间降序", notes = "开放接口")
    @ApiIgnore
    public R<List<Activity>> getAllActivity() {
        List<Activity> activities =
                service.list(new LambdaQueryWrapper<Activity>().orderByDesc(Activity::getCreateTime));
        return R.data(activities);

    }

    @GetMapping("/{activityId}")
    @ApiOperation(value = "根据活动id获取活动", notes = "开放接口")
    @ApiImplicitParam(name = "activityId", value = "活动id", required = true, paramType = "path")
    public R<Activity> getActivityById(@PathVariable Long activityId) {
        if (ObjectUtils.isEmpty(activityId)) {
            return R.fail("活动id不能为空");
        }
        Activity activity = service.getById(activityId);
        if (ObjectUtils.isEmpty(activity)) {
            return R.fail("不存在id为" + activityId + "的活动");
        }
        return R.data(activity);
    }

}
