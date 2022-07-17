package com.chaoshan.controller;

import cn.hutool.core.util.StrUtil;
import com.chaoshan.entity.TopicArticle;
import com.chaoshan.service.impl.TopicArticleCommentServiceImpl;
import com.chaoshan.service.impl.TopicArticleServiceImpl;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-19  12:57
 * @Description: TopicArticleController
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/topicArticle")
public class TopicArticleController {

    @Autowired
    private TopicArticleServiceImpl service;
    @Autowired
    private TopicArticleCommentServiceImpl topicArticleCommentService;

    @GetMapping("/complete/{articleId}")
    @ApiOperation(value = "根据id获取话题文章详情", notes = "开放接口，根据话题文章id，返回话题文章的内容还有对应的所有评论")
    @ApiImplicitParam(name = "articleId", value = "话题文章的id", required = true, paramType = "path")
    public R<Map<String, Object>> getArticleCompleteById(@PathVariable("articleId") Long articleId) {
        if (ObjectUtils.isEmpty(articleId)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("article", service.getTopicArticleVOById(articleId));
        map.put("comments", topicArticleCommentService.getArticleCommentVOList(articleId));
        return R.data(map);
    }

    @PostMapping("/star/{articleId}")
    @ApiOperation(value = "点赞话题文章")
    @ApiImplicitParam(name = "articleId", value = "话题文章的id", required = true, paramType = "path")
    public R starTopicArticle(@PathVariable("articleId") Long articleId) {
        return service.starTopicArticle(articleId);
    }

    @GetMapping("/simple/{articleId}")
    @ApiIgnore
    @ApiOperation(value = "根据id获取话题文章", notes = "开放接口")
    public R<TopicArticle> getArticleById(@PathVariable("articleId") Long articleId) {
        if (!ObjectUtils.isEmpty(articleId)) {
            return R.data(service.getById(articleId));
        }
        return null;
    }

    @PostMapping("/publish")
    @ApiIgnore
    @ApiOperation(value = "用户发布话题文章")
    public R publishTopicArticle(@RequestBody TopicArticle article) {
        if (ObjectUtils.isEmpty(article)) {
            return R.fail("发布内容不能为空");
        }
        if (StrUtil.isEmpty(article.getAccountid())) {
            article.setAccountid(LoginUser.getCurrentLoginUser().getAccountid());
        }
        if (service.save(article)) {
            return R.success("发布文章成功");
        }
        return R.fail("发布文章失败");
    }
}
