package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.MessageDTO;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.util.api.R;

import java.util.List;

/**
 * <p>
 * 用户消息表 服务类
 * </p>
 *
 * @author Hyx
 * @since 2022-05-13
 */
public interface IUserMessageService extends IService<UserMessage> {


    /**
     * 获取点赞与收藏消息列表
     */
    R<List<MessageDTO>> getStarCollList(String accountid);

    /**
     * 获取评论和回复消息列表
     * accountid -> 当前用户为接收方账号
     */
    R<List<MessageDTO>> getCommentList(String accountid);
}
