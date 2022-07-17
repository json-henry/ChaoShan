package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.Fans;
import com.chaoshan.entity.User;
import com.chaoshan.util.api.R;

import java.util.List;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */
public interface FansService extends IService<Fans> {

    /**
     * 判断用户是否已关注
     */
    boolean checkFocus(String accountid, String accountedid);

    /**
     * 进行关注/取关操作
     * accountid-->被关注账号
     * accountided-->当前用户账号（作为粉丝）
     */
    R focus(String accountid, String accountided) throws InterruptedException;

    /**
     * 获取用户关注的用户信息
     *
     * @param accountid
     * @return
     */
    R<List<User>> getFocusInfo(String accountid);

    /**
     * 获取用户粉丝的用户信息
     *
     * @param accountid
     * @return
     */
    R<List<User>> getFansInfo(String accountid);

    /**
     * 获取用户粉丝的用户信息，按fans的时间降序排列
     */
    R<List<User>> getFansInfoByDesc(String accountid);
}
