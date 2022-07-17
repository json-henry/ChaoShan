package com.chaoshan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.InformationClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.Fans;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.FansMapper;
import com.chaoshan.mapper.UserMapper;
import com.chaoshan.mapper.UserMessageMapper;
import com.chaoshan.service.FansService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.chaoshan.constant.MessageConstant.FOCUS_ACCOUNTID;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */
@Service
public class FansServiceImpl extends ServiceImpl<FansMapper, Fans> implements FansService {

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private InformationClient informationClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 判断用户是否已关注
     *
     * @param accountedid 当前用户
     * @param accountid   被关注用户
     * @return
     */
    @Override
    public boolean checkFocus(String accountedid, String accountid) {
        Fans fans = fansMapper.selectOne(new LambdaQueryWrapper<Fans>()
                .eq(Fans::getAccountid, accountid)
                .eq(Fans::getAccountided, accountedid));
        if (fans != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 进行关注/取关操作
     * accountid-->被关注账号
     * accountided-->当前用户账号（作为粉丝）
     */
    @Override
    public R focus(String accountid, String accountided) throws InterruptedException {
        if (ObjectUtil.hasEmpty(accountid, accountided) && accountid.equals(accountided)) {
            return R.fail(ResultCode.REQ_REJECT);
        }
        if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getAccountid, accountid)) == 0) {
            return R.fail("账号不存在");
        }
//        用户作为粉丝--》accountided
        if (!accountid.equals(accountided)) {
            Integer count = fansMapper.selectCount(new LambdaQueryWrapper<Fans>()
                    .eq(Fans::getAccountided, accountided)
                    .eq(Fans::getAccountid, accountid));
//        若查找的accountid与传进来的accountid一致，则为取消关注
            if (count > 0) {
                fansMapper.delete(new LambdaQueryWrapper<Fans>()
                        .eq(Fans::getAccountid, accountid)
                        .eq(Fans::getAccountided, accountided));
            } else {
                int insertCount = fansMapper.insert(new Fans()
                        .setAccountid(accountid)
                        .setAccountided(accountided));
                //添加新的消息数据-->类型为关注
                if (insertCount == 0) {
                    return R.data(ResultCode.FAILURE);
                }
                UserMessage userMessage = new UserMessage()
                        .setSendAccountid(accountided)
                        .setReceiveAccountid(accountid)
                        .setMessageType(FOCUS_ACCOUNTID);

                rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE, MessageConstant.MESSAGE_INSERT_KEY,
                        userMessage);
            }
            Fans fans =
                    fansMapper.selectOne(new LambdaQueryWrapper<Fans>().eq(Fans::getAccountid, accountid).eq(Fans::getAccountided, accountided));
            return R.data(fans);
        }
        return R.fail(ResultCode.REQ_REJECT);

    }

    /**
     * 获取用户关注的用户信息
     *
     * @param accountid
     * @return
     */
    @Override
    public R<List<User>> getFocusInfo(String accountid) {
        //      用户作为粉丝
        List<Fans> focus = fansMapper.selectList(new LambdaQueryWrapper<Fans>().eq(Fans::getAccountided, accountid));
        List<User> users = new ArrayList<>();
        //     查找关注用户的账号
        for (Fans fans : focus) {
            User userAccount = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccountid,
                    fans.getAccountid()));
            users.add(userAccount);
        }
        return R.data(users);
    }

    /**
     * 获取用户的粉丝信息
     *
     * @param accountid
     * @return
     */
    @Override
    public R<List<User>> getFansInfo(String accountid) {
        // 1. 用户为被关注者
        List<Fans> fans = fansMapper.selectList(new LambdaQueryWrapper<Fans>().eq(Fans::getAccountid, accountid));
        List<User> users = new ArrayList<>();
        // 2. 返回用户的头像，username，简介 信息
        for (Fans fan : fans) {
            User userInfo = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccountid,
                    fan.getAccountided()));
            //        查看当前用户是否也关注了该粉丝
            Fans isfans = fansMapper.selectOne(new LambdaQueryWrapper<Fans>().eq(Fans::getAccountided, accountid)
                    .eq(Fans::getAccountid, fan.getAccountided()));
            if (!ObjectUtil.isEmpty(userInfo)) {
                boolean isFocus = true;
                if (!ObjectUtil.isEmpty(isfans)) {
                    userInfo.setIsFocus(isFocus);
                } else {
                    userInfo.setIsFocus(!isFocus);
                }
                users.add(userInfo);
            }
        }
        return R.data(users);
    }

    /**
     * 获取用户粉丝的用户信息，按fans的时间降序排列
     *
     * @param accountid
     * @return
     */
    @Override
    public R<List<User>> getFansInfoByDesc(String accountid) {
//        根据用户账号返回所有的粉丝
        List<Fans> fans =
                fansMapper.selectList(new LambdaQueryWrapper<Fans>().eq(Fans::getAccountid, accountid).orderByDesc(Fans::getCreateTime));
        List<UserMessage> messages =
                userMessageMapper.selectList(new LambdaQueryWrapper<UserMessage>().eq(UserMessage::getReceiveAccountid,
                        accountid));
        //        获取该用户的粉丝数
        List<UserMessage> userMessages = messages
                .stream()
                .filter(message -> (message.getSendAccountid() != null) && (message.getCreateTime() != null))
                .filter(message -> (message.getMessageType().equals(FOCUS_ACCOUNTID)))
                .collect(Collectors.toList());
//        根据查到的accountided 查找用户信息
        List<User> users = new ArrayList<>();
        for (Fans fan : fans) {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccountid,
                    fan.getAccountided()));
            user.setCreateTime(fan.getCreateTime());
            users.add(user);
        }
        for (UserMessage userMessage : userMessages) {
            userMessage.setIsRead(true);
            userMessageMapper.updateById(userMessage);
        }
        return R.data(users);
    }
}
