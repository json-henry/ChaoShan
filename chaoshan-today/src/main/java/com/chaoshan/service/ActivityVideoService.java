package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ActivityVideo;

import java.util.List;

/**
 * @author YCE
 * @description 针对表【cs_activity_video(活动视频表)】的数据库操作Service
 * @createDate 2022-05-17 17:35:57
 */
public interface ActivityVideoService extends IService<ActivityVideo> {
    /**
     * 根据大小获取视频列表
     *
     * @param size
     * @return
     */
    List<ActivityVideo> getVideoListBySize(int size);

}
