package com.chaoshan.service;

import com.chaoshan.util.api.R;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-20  13:59
 * @Description: TodayService
 * @Version: 1.0
 */
public interface TodayService {


    /**
     * 查询搜索的内容，返回 话题 活动 离我最近的开放景区
     *
     * @param latitude
     * @param longitude
     * @param searchKey
     * @return
     */
    Map<String, Object> getTodayAllBySearchKey(String latitude, String longitude, String searchKey);

    /**
     * 查询今日潮汕首页显示的内容
     *
     * @param longitude
     * @param latitude
     * @return
     */
    Map<String, Object> getTodayHomePage(String latitude, String longitude);

    /**
     * 获取今日潮汕搜索框点击后显示的热搜数据
     *
     * @return
     */
    List<ZSetOperations.TypedTuple<String>> getHotSearch();

    R<Map<String, Object>> getAppointmentAndSignup();

    R<Map<String, Object>> getAllByEmptySearchKey(String latitude, String longitude);

    Long getAppointmentAndSignupTotal();

}
