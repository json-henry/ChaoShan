package com.chaoshan.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.Activity;
import com.chaoshan.entity.ActivityVideo;
import com.chaoshan.mapper.ActivityMapper;
import com.chaoshan.mapper.ActivityVideoMapper;
import com.chaoshan.service.ActivityService;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.chaoshan.constant.ESConstant.ACTIVITY_INDEX;

/**
 * @author YCE
 * @description 针对表【cs_activity(活动表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:35:57
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
        implements ActivityService {

    @Autowired
    private ActivityMapper mapper;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ActivityVideoMapper videoMapper;

    /**
     * 根据标题进行模糊查询活动
     *
     * @param title 标题
     * @return
     */
    @Override
    public List<Activity> getActivityByTitle(String title) {
        if (StrUtil.isEmpty(title)) {
            return null;
        }
        return mapper.selectList(new LambdaQueryWrapper<Activity>().like(Activity::getTitle, title));
    }

    /**
     * 获取全部活动，并按时间降序
     *
     * @return
     */
    @Override
    public List<Activity> getAllActivity() {
        //获取今天开始的时间
        LocalDateTime TodayStartTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
        //获取从今往后的活动并按开始时间升序
        List<Activity> activityList =
                mapper.selectList(new LambdaQueryWrapper<Activity>()
                        .select(Activity::getId, Activity::getTitle, Activity::getPictureLink,
                                Activity::getDetails, Activity::getStartTime, Activity::getEndTime,
                                Activity::getCreateTime, Activity::getCalendar, Activity::getLunar)
                        .orderByAsc(Activity::getCalendar));
        return activityList;
    }


    /**
     * 获取从今往后要发生的活动
     *
     * @return
     */
    @Override
    public List<Activity> getActivityList() {
        //获取今天开始的时间
        LocalDateTime TodayStartTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
        //获取从今往后的活动并按开始时间升序
        List<Activity> activityList =
                mapper.selectList(new LambdaQueryWrapper<Activity>()
                        .ge(Activity::getCalendar, TodayStartTime)
                        .orderByAsc(Activity::getCalendar));
        for (Activity activity : activityList) {
            List<ActivityVideo> activityVideos =
                    videoMapper.selectList(new LambdaQueryWrapper<ActivityVideo>().eq(ActivityVideo::getActivityid,
                            activity.getId()));
            activity.setVideos(activityVideos);
        }
        return activityList;
    }

    /**
     * 获取从今往后会发生的 #size 个活动
     *
     * @param size 获取多少个活动
     * @return
     */
    @Override
    public List<Activity> getActivityListBySize(Integer size) {
        //获取今天开始的时间
        LocalDateTime TodayStartTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
        //获取从今往后的活动并按开始时间升序
        List<Activity> activityList =
                mapper.selectList(new LambdaQueryWrapper<Activity>()
                        .select(Activity::getId, Activity::getTitle, Activity::getPictureLink,
                                Activity::getDetails, Activity::getStartTime, Activity::getEndTime,
                                Activity::getCreateTime, Activity::getCalendar, Activity::getLunar)
                        .ge(Activity::getCreateTime, TodayStartTime)
                        .orderByAsc(Activity::getStartTime)
                        .last("limit 0," + size));
        return activityList;
    }


    @Override
    @SneakyThrows
    public List<Activity> getActivityListBySearchKey(String searchKey) {
        SearchRequest request = new SearchRequest(ACTIVITY_INDEX);
        request.source()
                .query(QueryBuilders.matchQuery("all", searchKey))
                .sort("calendar", SortOrder.ASC)
                .size(500);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<Activity> activities = new ArrayList<>();
        for (SearchHit hit : hits) {
            Activity activity = JSON.parseObject(hit.getSourceAsString(), Activity.class);
            activities.add(activity);
        }
        return activities;
    }

}




