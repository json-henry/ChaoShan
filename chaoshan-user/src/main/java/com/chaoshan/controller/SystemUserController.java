package com.chaoshan.controller;

import cn.hutool.core.util.ObjectUtil;
import com.chaoshan.clients.InformationClient;
import com.chaoshan.clients.TodayClient;
import com.chaoshan.entity.*;
import com.chaoshan.service.AliyunOssServiceImpl;
import com.chaoshan.service.FansService;
import com.chaoshan.service.UserService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 14:07
 */
@RestController
@RequestMapping("/system")
public class SystemUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AliyunOssServiceImpl aliyunOssService;

    @Autowired
    private InformationClient informationClient;

    @Autowired
    private FansService fansService;

    @Autowired
    private TodayClient todayClient;

    /**
     * 多个图片视频文件上传
     */
    @ApiOperation("上传多个图片文件")
    @PostMapping(value = "/postManyFile")
    @ApiIgnore
    public R uploadMany(MultipartFile[] files) {
        List<String> pictures = new ArrayList<>();
//        if (files != null && files.length > 0) {
        for (MultipartFile file : files) {
            System.out.println(file.getOriginalFilename());
        }
//        }
        return R.data(pictures);
    }

    /**
     * 测试上传
     *
     * @param file
     * @return
     */
    @PostMapping("/testPhoto")
    @ApiIgnore
    public R<String> upload(MultipartFile file) {
        if (file != null) {
            return R.data(aliyunOssService.selectExist(file));
        }
        return R.fail("文件为空");
    }

    /**
     * 测试删除
     *
     * @return
     */
    @PostMapping("/testDelete")
    @ApiIgnore
    public R delete(@RequestBody UserMessage userMessage) {
        System.out.println(fansService.save(new Fans().setAccountid("111222")));
        return R.data(informationClient.addMessage(userMessage));
    }

    /**
     * 管理员新增景区
     */
    @PostMapping("/add/scenic")
    @ApiOperation("管理员新增景区")
    public R addScenic(@RequestBody Openscenic openscenic) {
        return todayClient.addScenic(openscenic);
    }

    /**
     * 管理员新增活动
     */
    @PostMapping("/add/activity")
    @ApiOperation(value = "新增活动")
    public R addActivity(@RequestBody Activity activity) {
        if (!ObjectUtil.isEmpty(activity.getCalendar())) {
            return todayClient.addActivity(activity);
        }

        return R.data(ResultCode.PARAM_MISS);
    }


    /**
     * 新增话题
     */
    @PostMapping("/add/topic")
    @ApiOperation("新增话题")
    public R addTopic(@RequestBody Topic topic) {
        return todayClient.publishTopic(topic);
    }
}
