package com.chaoshan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.ArticleClient;
import com.chaoshan.clients.TodayClient;
import com.chaoshan.clients.UserClient;
import com.chaoshan.entity.*;
import com.chaoshan.mapper.UserMessageMapper;
import com.chaoshan.service.IUserMessageService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.chaoshan.constant.MessageConstant.*;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * <p>
 * 用户消息表 服务实现类
 * </p>
 *
 * @author Hyx
 * @since 2022-05-13
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

    @Autowired
    UserMessageMapper messageMapper;

    @Autowired
    private ArticleClient articleClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private TodayClient todayClient;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据输入消息类型获取对应的未读消息数
     *
     * @param types
     * @return
     */
    public Map<String, Long> getMessageNumberMapByType(int... types) {
        String accountid = LoginUser.getCurrentLoginUser().getAccountid();
        List<UserMessage> list = messageMapper.getUserMessageListByType(types, accountid);
        HashMap<String, Long> map = new HashMap<>();

        for (int type : types) {
            String description = DESCRIPTION.get(type);
            long count = list.stream()
                    .filter(userMessage -> !userMessage.getIsRead() && userMessage.getMessageType() == type)
                    .count();
            if (!map.containsKey(description)) {
                map.put(description, 0L);
            }
            map.put(description, map.get(description) + count);
        }
        map.put("total", (long) list.size());

        return map;
    }

    /**
     * 获取文章和话题点赞  文章评论回复点赞
     * 与
     * 文章收藏消息列表   话题评论回复点赞
     */
    @Override
    public R<List<MessageDTO>> getStarCollList(String accountid) {
        List<MessageDTO> messageDTOS = new ArrayList<>();
//        获取该用户所有未读的消息
        List<UserMessage> messages =
                messageMapper.selectList(new LambdaQueryWrapper<UserMessage>().eq(UserMessage::getReceiveAccountid,
                        accountid));


//        获取文章点赞和收藏的消息 || 获取文章评论和回复点赞的消息
        List<UserMessage> articleMsgs = messages
                .stream()
                .filter(message -> (message.getSendAccountid() != null) && (message.getArticleid() != null) && (message.getCreateTime() != null))
                .filter(message -> (message.getMessageType().equals(ARTICLE_STAR_MESSAGE)) || (message.getMessageType().equals(ARTICLE_COLLECTION_MESSAGE)) || (message.getMessageType().equals(ARTICLE_STAR_COMMENT_MESSAGE)))
                .collect(Collectors.toList());
//        获取到的均为用户发表文章类型，故需要获取
//      返回点赞||收藏的消息内容
//        根据消息类型，均为文章类型的点赞和收藏，故articleid为用户发表文章id（Article）
        for (UserMessage articleMsg : articleMsgs) {
//            查找发送方用户的信息
            User user = userClient.getInfoByAccountid(articleMsg.getSendAccountid());
            redisTemplate.opsForValue().set("user", user);
//            根据消息的文章id查找文章信息
            Article article = articleClient.getArticleDetail(articleMsg.getArticleid().intValue()).getData();
            MessageDTO messageDTO = new MessageDTO(user.getUsername(), user.getAvatar(), articleMsg.getArticleid(),
                    article.getTitle(), article.getPictureLink(),
                    articleMsg.getMessageType(), articleMsg.getCreateTime());
            articleMsg.setIsRead(true);
            messageMapper.updateById(articleMsg);
            messageDTOS.add(messageDTO);
        }

//        获取话题文章点赞的消息 和 话题评论回复的消息
        List<UserMessage> topicArticles = messages
                .stream()
                .filter(message -> (message.getSendAccountid() != null) && (message.getArticleid() != null) && (message.getCreateTime() != null))
                .filter(message -> (message.getMessageType().equals(TOPIC_STAR_MESSAGE)) || (message.getMessageType().equals(TOPIC_STAR_COMMENT_MESSAGE)))
                .collect(Collectors.toList());
//        根据消息类型，为话题文章类型的点赞，故articleid为话题文章id（TopicArticle）
        for (UserMessage topicArticle : topicArticles) {
//           查找发送方用户信息
            User user = userClient.getInfoByAccountid(topicArticle.getSendAccountid());
//            根据话题的文章id查找话题文章信息
            TopicArticle topicInfo = todayClient.getArticleById(topicArticle.getArticleid()).getData();
            log.print("===============" + user.getUsername());
            MessageDTO topicMsgDTO = new MessageDTO(user.getUsername(), user.getAvatar(), topicArticle.getArticleid(),
                    topicInfo.getContent(), topicInfo.getPictureLink(),
                    topicArticle.getMessageType(), topicArticle.getCreateTime());
            topicArticle.setIsRead(true);
            messageMapper.updateById(topicArticle);
            messageDTOS.add(topicMsgDTO);
        }
//
        return R.data(messageDTOS);
    }

    /**
     * 获取文章评论和回复消息列表
     * 获取话题文章评论和回复消息列表
     *
     * @param accountid 当前已登录的用户id
     * @return
     */
    @Override
    public R<List<MessageDTO>> getCommentList(String accountid) {
        List<MessageDTO> messageDTOS = new ArrayList<>();
//        获取该用户的相关消息
        List<UserMessage> messages =
                messageMapper.selectList(new LambdaQueryWrapper<UserMessage>().eq(UserMessage::getReceiveAccountid,
                        accountid));

//        获取文章评论和回复的消息
        List<UserMessage> userMessages = messages
                .stream()
                .filter(message -> (message.getSendAccountid() != null) && (message.getArticleid() != null) && (message.getCreateTime() != null) && (message.getMessage() != null))
                .filter(message -> (message.getMessageType().equals(ARTICLE_COMMENT_MESSAGE)) || (message.getMessageType().equals(ARTICLE_REPLY_MESSAGE)))
                .collect(Collectors.toList());
//        获取文章信息
        for (UserMessage message : userMessages) {
//            查找发送方用户的信息ouyo
            User user = userClient.getInfoByAccountid(message.getSendAccountid());
//            根据消息的文章id查找文章信息
            Article article = articleClient.getArticleDetail(message.getArticleid().intValue()).getData();
            if (!ObjectUtil.isEmpty(article)) {
                MessageDTO messageDTO = new MessageDTO(user.getUsername(), user.getAvatar(), message.getArticleid(),
                        article.getTitle(), article.getPictureLink(),
                        message.getMessageType(), message.getMessage(), message.getCreateTime());
                message.setIsRead(true);
                messageMapper.updateById(message);
                messageDTOS.add(messageDTO);
            }
        }
//        获取话题评论和回复的消息
        List<UserMessage> messageList = messages
                .stream()
                .filter(message -> (message.getSendAccountid() != null) && (message.getArticleid() != null) && (message.getCreateTime() != null) && (message.getMessage() != null))
                .filter(message -> (message.getMessageType().equals(TOPIC_COMMENT_MESSAGE)) || (message.getMessageType().equals(TOPIC_REPLY_MESSAGE)))
                .collect(Collectors.toList());
//        获得话题文章信息
        for (UserMessage userMessage : messageList) {
//            获得发送方用户的信息
            User user = userClient.getInfoByAccountid(userMessage.getSendAccountid());
            TopicArticle topicArticle = todayClient.getArticleById(userMessage.getArticleid()).getData();
            if (!ObjectUtil.isEmpty(topicArticle)) {
                MessageDTO topicMsg = new MessageDTO(user.getUsername(), user.getAvatar(), userMessage.getArticleid(),
                        topicArticle.getContent(), topicArticle.getPictureLink(),
                        userMessage.getMessageType(), userMessage.getMessage(), userMessage.getCreateTime());
                userMessage.setIsRead(true);
                messageMapper.updateById(userMessage);
                messageDTOS.add(topicMsg);
            }

        }

        return R.data(messageDTOS);
    }
}
