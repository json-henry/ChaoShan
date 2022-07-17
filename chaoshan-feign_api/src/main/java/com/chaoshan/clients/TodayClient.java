package com.chaoshan.clients;

import com.chaoshan.clients.fallback.TodayClientFallbackFactory;
import com.chaoshan.entity.Activity;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.entity.Topic;
import com.chaoshan.entity.TopicArticle;
import com.chaoshan.util.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 21:16
 */
@FeignClient(value = "CHAOSHAN-TODAY", fallback = TodayClientFallbackFactory.class)
public interface TodayClient {

    String prefix_ = "/today/api";

    /**
     * 获取用户的预约和报名总数
     */
    @GetMapping(prefix_ + "/today/appointmentAndSignup/total")
    Long getAppointmentAndSignupTotal();


    /**
     * 获取所有的活动，按国历时间降序
     *
     * @return
     */
    @GetMapping(prefix_ + "/activity/all")
    R<List<Activity>> getAllActivity();

    /**
     * 获取所有的话题，按时间降序排序
     *
     * @return
     */
    @GetMapping(prefix_ + "/topic/all")
    R<List<Topic>> getAllTopics();


    /**
     * 根据文章id返回话题文章的相关信息
     *
     * @param articleId
     * @return
     */
    @GetMapping(prefix_ + "/topicArticle/simple/{articleId}")
    R<TopicArticle> getArticleById(@PathVariable("articleId") Long articleId);

    /**
     * 在处理完图片上传后调用该接口进行文章发布
     *
     * @param article
     * @return
     */
    @PostMapping(prefix_ + "/topicArticle/publish")
    R publishTopicArticle(@RequestBody TopicArticle article);

    /**
     * 后台管理员新增活动
     *
     * @param activity
     * @return
     */
    @PostMapping("/today/system/activity/add")
    R addActivity(@RequestBody Activity activity);

    /**
     * 管理员新增景区
     *
     * @param scenic
     * @return
     */
    @PostMapping("/today/system/scenic/add")
    R addScenic(@RequestBody Openscenic scenic);

    /**
     * 管理新增话题
     *
     * @param topic
     * @return
     */
    @PostMapping("/today/system/topic/publish")
    R publishTopic(@RequestBody Topic topic);
}
