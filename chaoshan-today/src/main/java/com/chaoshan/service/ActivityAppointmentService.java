package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.ActivityAppointment;
import com.chaoshan.util.api.R;

/**
 * @author YCE
 * @description 针对表【cs_activity_appointment(活动预约表
 * )】的数据库操作Service
 * @createDate 2022-05-17 17:35:57
 */
public interface ActivityAppointmentService extends IService<ActivityAppointment> {

    /**
     * 活动预约
     *
     * @param activityId
     * @return
     */
    R appointment(Long activityId);

    /**
     * 活动取消预约
     *
     * @param activityId
     * @return
     */
    R unAppointment(Long activityId);
}
