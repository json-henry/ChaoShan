package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.Activity;

import java.util.List;

/**
 * @author YCE
 * @description 针对表【cs_activity(活动表)】的数据库操作Service
 * @createDate 2022-05-17 17:35:57
 */
public interface ActivityService extends IService<Activity> {
    /**
     * 根据标题进行模糊查询活动
     *
     * @param title 标题
     * @return
     */
    List<Activity> getActivityByTitle(String title);

    /**
     * 获取从今往后要发生的活动
     *
     * @return
     */
    List<Activity> getActivityList();


    /**
     * 获取从今往后会发生的 #size 个活动
     *
     * @param size 获取多少个活动
     * @return
     */
    List<Activity> getActivityListBySize(Integer size);

    /**
     * 根据搜索关键字获取活动列表
     *
     * @param searchKey
     * @return
     */
    List<Activity> getActivityListBySearchKey(String searchKey);

    /**
     * 获取所有的活动
     *
     * @return
     */
    List<Activity> getAllActivity();
}
