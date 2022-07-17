package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.ActivityVideo;
import com.chaoshan.mapper.ActivityVideoMapper;
import com.chaoshan.service.ActivityVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author YCE
 * @description 针对表【cs_activity_video(活动视频表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:35:57
 */
@Service
public class ActivityVideoServiceImpl extends ServiceImpl<ActivityVideoMapper, ActivityVideo>
        implements ActivityVideoService {

    @Autowired
    private ActivityVideoMapper mapper;

    /**
     * 根据大小获取视频列表
     *
     * @param size
     * @return
     */
    @Override
    public List<ActivityVideo> getVideoListBySize(int size) {
        LambdaQueryWrapper<ActivityVideo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ActivityVideo::getCreateTime)
                .last("limit 0," + size);
        return mapper.selectList(wrapper);
    }
}




