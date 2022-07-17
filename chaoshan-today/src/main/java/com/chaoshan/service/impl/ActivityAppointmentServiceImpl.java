package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.ActivityAppointment;
import com.chaoshan.mapper.ActivityAppointmentMapper;
import com.chaoshan.service.ActivityAppointmentService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author YCE
 * @description 针对表【cs_activity_appointment(活动预约表
 * )】的数据库操作Service实现
 * @createDate 2022-05-17 17:35:57
 */
@Service
public class ActivityAppointmentServiceImpl extends ServiceImpl<ActivityAppointmentMapper, ActivityAppointment>
        implements ActivityAppointmentService {
    @Autowired
    private ActivityAppointmentMapper mapper;

    @Override
    public R appointment(Long activityId) {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        if (isAppointment(activityId, accountId)) {
            return R.fail("该活动已预约！");
        }
        if (mapper.insert(new ActivityAppointment(activityId, accountId)) > 0) {
            return R.success("预约成功");
        }
        return R.fail("预约失败");
    }

    @Override
    public R unAppointment(Long activityId) {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        if (!isAppointment(activityId, accountId)) {
            return R.fail("未预约过这个活动");
        }
        if (mapper.delete(new LambdaQueryWrapper<ActivityAppointment>()
                .eq(ActivityAppointment::getActivityid, activityId)
                .eq(ActivityAppointment::getAccountid, accountId)
        ) > 0) {
            return R.success("取消预约成功");
        }
        return R.fail("取消预约失败");
    }

    private boolean isAppointment(Long activityId, String accountId) {
        ActivityAppointment appointment = mapper.selectOne(new LambdaQueryWrapper<ActivityAppointment>()
                .eq(ActivityAppointment::getActivityid, activityId)
                .eq(ActivityAppointment::getAccountid, accountId)
        );
        return !ObjectUtils.isEmpty(appointment);
    }
}




