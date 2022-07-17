package com.chaoshan.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.constant.RedisConstant;
import com.chaoshan.entity.*;
import com.chaoshan.entity.doc.OpenscenicDoc;
import com.chaoshan.service.TodayService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.chaoshan.constant.TodayConstant.*;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-20  14:00
 * @Description: TodayServiceImpl
 * @Version: 1.0
 */
@Service
public class TodayServiceImpl implements TodayService {
    @Autowired
    private OpenscenicServiceImpl scenicService;
    @Autowired
    private TopicServiceImpl topicService;
    @Autowired
    private ActivityVideoServiceImpl activityVideoService;
    @Autowired
    private ActivityServiceImpl activityService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OpenscenicSignupServiceImpl signupService;
    @Autowired
    private ActivityAppointmentServiceImpl appointmentService;


    /**
     * 查询搜索的内容，返回 话题 活动 离我最近的开放景区
     *
     * @param searchKey
     * @return
     */
    @Override
    @SneakyThrows
    public Map<String, Object> getTodayAllBySearchKey(String latitude, String longitude, String searchKey) {
        Map<String, Object> resultMap = new HashMap<>();
        List<OpenscenicDoc> scenicList;
        if (!locationIsEmpty(latitude, longitude)) {
            String location = latitude + "," + longitude;
            scenicList = scenicService.getScenicListBySearchKey(location, searchKey);
        } else {
            scenicList = scenicService.getScenicListBySearchKey(searchKey);
        }
        resultMap.put("scenicList", scenicList);
        resultMap.put("activityList", activityService.getActivityListBySearchKey(searchKey));
        resultMap.put("topicList", topicService.getTopicListBySearchKey(searchKey));
        return resultMap;
    }

    /**
     * 查询今日潮汕首页显示的内容
     *
     * @param longitude
     * @param latitude
     * @return
     */
    @Override
    public Map<String, Object> getTodayHomePage(String latitude, String longitude) {
        //存放在今日潮汕首页上显示的内容
        Map<String, Object> map = new HashMap<>();

        map.put(HOMEPAGE_TOPIC_KEY, topicService.getTopicListByHotSort(HOMEPAGE_TOPIC_SIZE));
//        map.put(HOMEPAGE_ACTIVITY_VIDEO_KEY, activityVideoService.getVideoListBySize(HOMEPAGE_VIDEO_SIZE));
        map.put(HOMEPAGE_ACTIVITY_KEY, activityService.getActivityList());

        //判断地址是否为空
        if (locationIsEmpty(latitude, longitude)) {
            map.put(HOMEPAGE_SCENIC_KEY, scenicService.getScenicListBySize(HOMEPAGE_SCENIC_SIZE));
        } else {
            String location = latitude + "," + longitude;
            map.put(HOMEPAGE_SCENIC_KEY, scenicService.getScenicListByLocationAndSize(location,
                    HOMEPAGE_SCENIC_SIZE));
        }
        return map;
    }

    @Override
    public List<ZSetOperations.TypedTuple<String>> getHotSearch() {
        List<ZSetOperations.TypedTuple<String>> typedTuples =
                new ArrayList<>(Objects.requireNonNull(redisTemplate.opsForZSet().reverseRangeWithScores(RedisConstant.HOT_SEARCH_KEY, 0, -1)));
        return typedTuples;
    }

    @Override
    public R<Map<String, Object>> getAppointmentAndSignup() {
        Map<String, Object> map = new HashMap<>();
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        List<ActivityAppointment> appointments =
                appointmentService.list(new LambdaQueryWrapper<ActivityAppointment>().eq(ActivityAppointment::getAccountid,
                        accountId));

        List<OpenscenicSignup> signups =
                signupService.list(new LambdaQueryWrapper<OpenscenicSignup>().eq(OpenscenicSignup::getAccountid,
                        accountId));
        //给每个预约添加活动信息
        setActivityToAppointment(appointments);
        //给每个报名添加景区信息
        setOpenScenicToSignup(signups);

        map.put("appointments", appointments);
        map.put("signups", signups);
        return R.data(map);
    }

    /**
     * 给每个报名添加景区信息
     *
     * @param signups
     */
    private void setOpenScenicToSignup(List<OpenscenicSignup> signups) {
        Openscenic scenic;
        for (OpenscenicSignup signup : signups) {
            scenic = scenicService.getOne(new LambdaQueryWrapper<Openscenic>().eq(Openscenic::getId,
                    signup.getOpenscenicid()));
            signup.setOpenscenic(scenic);
        }
    }

    /**
     * 给每个预约添加活动信息
     *
     * @param appointments
     */
    private void setActivityToAppointment(List<ActivityAppointment> appointments) {
        Activity activity;
        for (ActivityAppointment appointment : appointments) {
            activity = activityService.getOne(new LambdaQueryWrapper<Activity>().eq(Activity::getId,
                    appointment.getActivityid()));
            appointment.setActivity(activity);
        }
    }


    @Override
    public Long getAppointmentAndSignupTotal() {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        int appointmentCount =
                appointmentService.count(new LambdaQueryWrapper<ActivityAppointment>().eq(ActivityAppointment::getAccountid,
                        accountId));
        int signupCount =
                signupService.count(new LambdaQueryWrapper<OpenscenicSignup>().eq(OpenscenicSignup::getAccountid,
                        accountId));
        return Long.valueOf(appointmentCount + signupCount);
    }

    @Override
    public R<Map<String, Object>> getAllByEmptySearchKey(String latitude, String longitude) {
        List<Activity> allActivity = activityService.getAllActivity();
        List<Topic> topicList = topicService.getTopicList();

        List<OpenscenicDoc> openingDocs;
        if (!locationIsEmpty(latitude, longitude)) {
            openingDocs = scenicService.getScenicListByLocation(latitude + "," + longitude);
        } else {
            openingDocs = scenicService.getScenicList();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("scenicList", openingDocs);
        map.put("activityList", allActivity);
        map.put("topicList", topicList);
        return R.data(map);
    }

    private boolean locationIsEmpty(String latitude, String longitude) {
        return StrUtil.isEmpty(longitude) || StrUtil.isEmpty(latitude);
    }
}
