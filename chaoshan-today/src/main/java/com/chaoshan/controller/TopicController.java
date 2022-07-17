package com.chaoshan.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.Topic;
import com.chaoshan.service.impl.TopicServiceImpl;
import com.chaoshan.util.api.R;
import com.chaoshan.utils.IpUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chaoshan.constant.RabbitMQConstant.TOPIC_VIEW_QUEUE_NAME;
import static com.chaoshan.constant.TodayConstant.MORE_PAGE_TOPIC_SIZE;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-18  22:13
 * @Description: 话题相关接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/topic")
public class TopicController {

    @Autowired
    private TopicServiceImpl service;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/morePage")
    @ApiOperation(value = "获取热门话题的更多页", notes = "开放接口")
    public R<List<Topic>> getTopics() {
        return R.data(service.getTopicListByHotSort(MORE_PAGE_TOPIC_SIZE));
    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索相关话题", notes = "开放接口")
    @ApiImplicitParam(name = "searchKey", value = "搜索关键字", required = false, paramType = "query")
    public R<List<Topic>> getTopicBySearchKey(@RequestParam(value = "searchKey", required = false) String searchKey) {
        if (StrUtil.isEmpty(searchKey)) {
            return R.data(service.getTopicList());
        }
        return R.data(service.getTopicListBySearchKey(searchKey));
    }

    @GetMapping("/{topicId}")
    @ApiOperation(value = "获取话题的详情", notes = "开放接口，根据话题id会返回话题以及对应的所有话题文章，包括点赞等等")
    @ApiImplicitParam(name = "topicId", value = "话题id", required = true, paramType = "path")
    public R<Map<String, Object>> getTopicById(@PathVariable("topicId") Long topicId, HttpServletRequest request) {
        Map<String, Object> map = service.getTopicAndArticleById(topicId);
        //发送消息到MQ，处理增加该话题的浏览量
        if (!CollectionUtils.isEmpty(map)) {
            Map<String, String> message = new HashMap<>();
            message.put("topicId", String.valueOf(topicId));
            message.put("accountId", IpUtil.getIpAddress(request));
            rabbitTemplate.convertAndSend(TOPIC_VIEW_QUEUE_NAME, JSONUtil.toJsonStr(message));
        }
        return R.data(map);
    }

    @GetMapping("/all")
    @ApiIgnore
    @ApiOperation(value = "获取所有的话题，并按时间降序", notes = "开放接口")
    public R<List<Topic>> getAllTopic() {
        List<Topic> list = service.list(new LambdaQueryWrapper<Topic>().orderByDesc(Topic::getCreateTime));
        return R.data(list);
    }
}
