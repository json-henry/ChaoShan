package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.UserInvitation;
import com.chaoshan.util.api.R;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */
public interface UserInvitationService extends IService<UserInvitation> {

    /**
     * 被邀请用户首次登录时是否提供了邀请码
     */
    R insertInvite(String code, String accountIded);

}
