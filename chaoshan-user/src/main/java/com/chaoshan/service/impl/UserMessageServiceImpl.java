package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.UserMessageMapper;
import com.chaoshan.service.UserMessageService;
import org.springframework.stereotype.Service;

/**
 * @author 呱呱
 * @date Created in 2022/5/15 17:44
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

}
