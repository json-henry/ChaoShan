package com.chaoshan.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.chaoshan.service.impl.TodayServiceImpl;
import com.chaoshan.util.api.R;
import com.chaoshan.utils.IpUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chaoshan.constant.RabbitMQConstant.HOT_SEARCH_QUEUE_NAME;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-17  17:49
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/today")
public class TodayController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TodayServiceImpl service;

    @GetMapping("/search")
    @ApiOperation(value = "今日潮汕首页搜索内容", notes = "开放接口，同时搜索话题、活动、景区内容")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "searchKey", value = "搜索关键字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = false, paramType = "query")
    })
    public R<Map<String, Object>> searchAllByKey(
            @RequestParam(value = "searchKey", required = false) String searchKey,
            @RequestParam(value = "latitude", required = false) String latitude,
            @RequestParam(value = "longitude", required = false) String longitude,
            HttpServletRequest request) throws IOException {
        if (StrUtil.isEmpty(searchKey)) {
            return service.getAllByEmptySearchKey(latitude, longitude);
        }

        Map<String, Object> resultMap = service.getTodayAllBySearchKey(latitude, longitude, searchKey);

        String accountId = IpUtil.getIpAddress(request);
        Map<String, String> message = new HashMap<>();
        message.put("accountId", accountId);
        message.put("searchKey", searchKey);
        rabbitTemplate.convertAndSend(HOT_SEARCH_QUEUE_NAME, JSONUtil.toJsonStr(message));
        return R.data(resultMap);
    }

    @GetMapping("/homepage")
    @ApiOperation(value = "获取今日潮汕首页内容", notes = "开放接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "latitude", value = "纬度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = false, paramType = "query")
    })
    public R<Map<String, Object>> getHomePage(@RequestParam(value = "latitude", required = false) String latitude,
                                              @RequestParam(value = "longitude", required = false) String longitude) {
        Map<String, Object> map = service.getTodayHomePage(latitude, longitude);
        return R.data(map);
    }

    @GetMapping("/searchPage")
    @ApiOperation("点击搜索之后显示的热搜数据")
    public R<List<ZSetOperations.TypedTuple<String>>> getSearchPage() {
        return R.data(service.getHotSearch());
    }

    @GetMapping("/appointmentAndSignup")
    @ApiOperation("获取预约和报名")
    public R<Map<String, Object>> getAppointmentAndSignup() {
        return service.getAppointmentAndSignup();
    }

    @GetMapping("/appointmentAndSignup/total")
    @ApiOperation("获取当前登录的报名和预约的总数")
    @ApiIgnore
    public Long getAppointmentAndSignupTotal() {
        return service.getAppointmentAndSignupTotal();
    }


}
