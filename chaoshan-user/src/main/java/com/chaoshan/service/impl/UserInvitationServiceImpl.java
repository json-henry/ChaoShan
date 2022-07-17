package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserInvitation;
import com.chaoshan.mapper.UserInvitationMapper;
import com.chaoshan.mapper.UserMapper;
import com.chaoshan.service.UserInvitationService;
import com.chaoshan.util.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */
@Service
public class UserInvitationServiceImpl extends ServiceImpl<UserInvitationMapper, UserInvitation> implements UserInvitationService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInvitationMapper invitationMapper;

    /**
     * 被邀请用户首次登录时是否提供了邀请码
     */
    @Override
    public R insertInvite(String code, String accountIded) {
        //获取拥有该邀请码的用户
        User invitee = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getInvitationCode, code));
//        若被邀请用户与拥有该邀请码的用户相同则返回错误
        if (accountIded.equals(invitee.getAccountid())) {
            return R.data("被邀请用户与拥有该邀请码的用户相同");
        }
//        若被邀请用户已被邀请，同样不能实现
        Integer count =
                invitationMapper.selectCount(new LambdaQueryWrapper<UserInvitation>().eq(UserInvitation::getAccountided, accountIded));
        if (count > 0) {
//            accountIded已被邀请，不能重复被邀请
            return R.data("accountIded已被邀请，不能重复被邀请");
        }
        return R.data(invitationMapper.insert(new UserInvitation().setAccountid(invitee.getAccountid()).setAccountided(accountIded)));
    }
}
