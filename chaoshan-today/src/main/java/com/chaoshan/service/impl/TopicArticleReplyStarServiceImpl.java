package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.TopicArticleReply;
import com.chaoshan.entity.TopicArticleReplyStar;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.TopicArticleCommentMapper;
import com.chaoshan.mapper.TopicArticleReplyMapper;
import com.chaoshan.mapper.TopicArticleReplyStarMapper;
import com.chaoshan.service.TopicArticleReplyStarService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import com.chaoshan.utils.DateTimeUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * @author YCE
 * @description 针对表【cs_topic_article_reply_star(话题文章回复点赞表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:34:59
 */
@Service
public class TopicArticleReplyStarServiceImpl extends ServiceImpl<TopicArticleReplyStarMapper, TopicArticleReplyStar>
        implements TopicArticleReplyStarService {
    @Autowired
    private TopicArticleReplyStarMapper replyStarMapper;
    @Autowired
    private TopicArticleCommentMapper commentMapper;
    @Autowired
    private TopicArticleReplyMapper replyMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 点赞评论
     *
     * @param replyId
     * @return
     */
    @Override
    public R starReply(Long replyId) {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        TopicArticleReplyStar star = replyStarMapper.selectOne(new LambdaQueryWrapper<TopicArticleReplyStar>()
                .eq(TopicArticleReplyStar::getReplyId, replyId)
                .eq(TopicArticleReplyStar::getAccountid, accountId));

        if (!ObjectUtils.isEmpty(star)) {
            LocalDateTime updateTime = star.getUpdateTime();
            star.setIsStar(star.getIsStar() == 1 ? 0 : 1);
            replyStarMapper.updateById(star);

            if (star.getIsStar() == 0) {
                return R.success("取消点赞成功");
            }
            if ((ObjectUtils.isEmpty(updateTime) && !DateTimeUtils.isToday(star.getCreateTime())) || !DateTimeUtils.isToday(updateTime)) {
                sendMessage(replyId, accountId);
            }
        } else {
            star = new TopicArticleReplyStar(accountId, replyId);
            replyStarMapper.insert(star);
            sendMessage(replyId, accountId);
        }
        return R.success("点赞成功");
    }

    private void sendMessage(Long replyId, String accountId) {
        //发送点赞消息
        TopicArticleReply reply = replyMapper.selectById(replyId);
        Long articleId = commentMapper.selectById(reply.getArticleCommentId()).getArticleId();
        String receiveAccountId = reply.getAccountid();

        if (!accountId.equals(receiveAccountId)) {
            //封装消息对象
            UserMessage message = new UserMessage(articleId, accountId, receiveAccountId, reply.getContent(),
                    MessageConstant.TOPIC_STAR_COMMENT_MESSAGE);
            rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE, MessageConstant.MESSAGE_INSERT_KEY,
                    message);
        }
    }
}




