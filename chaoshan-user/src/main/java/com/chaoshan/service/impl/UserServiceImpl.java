package com.chaoshan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.TodayClient;
import com.chaoshan.entity.Fans;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserInvitation;
import com.chaoshan.entity.dto.UserDTO;
import com.chaoshan.entity.dto.UserPasswordDTO;
import com.chaoshan.mapper.FansMapper;
import com.chaoshan.mapper.UserInvitationMapper;
import com.chaoshan.mapper.UserMapper;
import com.chaoshan.mapper.UserMessageMapper;
import com.chaoshan.service.AliyunOssServiceImpl;
import com.chaoshan.service.UserService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import com.chaoshan.util.api.Rpage;
import com.chaoshan.util.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private UserInvitationMapper userInvitationMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private AliyunOssServiceImpl aliyunOssService;
    @Autowired
    private TodayClient todayClient;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 分页获取用户数据
     *
     * @param currentPage 当前页
     * @param size        数量
     * @return
     */
    @Override
    public R<Rpage> getUserByPage(Integer currentPage, Integer size) {
        Page<User> page = new Page<>(currentPage, size);
        IPage<User> userIPage = userMapper.getUserByPage(page);
        return R.data(new Rpage(userIPage.getTotal(), userIPage.getRecords()));
    }

    /**
     * 获取用户信息
     * 返回用户名、头像、简介及关注数、粉丝数、预约报名数及邀请用户数
     */
    @Override
    public R<UserDTO> getUserInfo(String accountid) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccountid, accountid));
//         根据accountid作为accountided查找计算关注了多少用户
        Integer followNum = fansMapper.selectCount(new LambdaQueryWrapper<Fans>().eq(Fans::getAccountided, accountid));
//          查找当前用户有多少预约报名数
        long signNum = todayClient.getAppointmentAndSignupTotal();
//         查找当前用户有多少粉丝数
        Integer fansNum = fansMapper.selectCount(new LambdaQueryWrapper<Fans>().eq(Fans::getAccountid, accountid));
//         计算邀请用户的数量
        Integer invitationNum =
                userInvitationMapper.selectCount(new LambdaQueryWrapper<UserInvitation>().eq(UserInvitation::getAccountid, accountid));
        return R.data(new UserDTO(accountid, user.getUsername(), user.getAvatar(), user.getIntroduction(),
                user.getIsBusiness(), followNum, user.getPhone(), signNum, fansNum, invitationNum));
    }

    /**
     * 获取被邀请用户信息列表
     *
     * @param accountid
     * @return
     */
    @Override
    public List<User> getInvitedUser(String accountid) {
//      根据当前用户账号查找被邀请的userInvitation数据列表
        List<UserInvitation> invitations = userInvitationMapper.selectList(new LambdaQueryWrapper<UserInvitation>()
                .eq(UserInvitation::getAccountid, accountid));
//        返回被邀请用户的信息
        List<User> userList = new ArrayList<>();
        if (!ObjectUtil.isEmpty(invitations)) {
            for (UserInvitation invitation : invitations) {
                userList.add(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccountid,
                        invitation.getAccountided())));
            }
        }
        return userList;
    }

    @Override
    public List<String> getAllAccountId() {
        List<User> users = userMapper.selectAll();
        List<String> accountids = new ArrayList<>();
        for (User user : users) {
            accountids.add(user.getAccountid());
        }
        return accountids;
    }

    @Override
    public R updateUserInfo(User user) {
        User oldUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccountid, user.getAccountid()));
//        若传入的user头像不为空且与旧头像不一样则删除旧头像
        if (!ObjectUtil.isEmpty(user.getAvatar()) && !user.getAvatar().equals(oldUser.getAvatar())) {
            aliyunOssService.deleteFile(oldUser.getAvatar());
        }
        return R.data(userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getAccountid,
                user.getAccountid())));
    }

    /**
     * 当前登录用户修改密码
     *
     * @param userPasswordDTO
     * @return
     */
    @Override
    public R modifyPassword(UserPasswordDTO userPasswordDTO) {
        // 查询当前登录用户
        User currentLoginUser = LoginUser.getCurrentLoginUser();
        // 根据用户查询用户加密的密码
        User user =
                userMapper.selectOne(new LambdaQueryWrapper<User>().select(User::getPassword).eq(User::getAccountid,
                        currentLoginUser.getAccountid()));
        if (ObjectUtil.isEmpty(user.getPassword())) {
            return R.fail(ResultCode.FAILURE);
        }
        if (passwordEncoder.matches(userPasswordDTO.getRawPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userPasswordDTO.getNewPassword()));
            if (userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getAccountid,
                    currentLoginUser.getAccountid())) > 0) {
                return R.success(ResultCode.SUCCESS, "密码修改成功!");
            }
        }
        return R.fail(ResultCode.FAILURE, "密码修改失败请联系管理员！");
    }
}