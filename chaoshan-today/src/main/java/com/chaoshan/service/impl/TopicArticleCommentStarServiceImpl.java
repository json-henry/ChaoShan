package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.TopicArticleComment;
import com.chaoshan.entity.TopicArticleCommentStar;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.TopicArticleCommentMapper;
import com.chaoshan.mapper.TopicArticleCommentStarMapper;
import com.chaoshan.service.TopicArticleCommentStarService;
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
 * @description 针对表【cs_topic_article_comment_star(话题文章评论点赞表 )】的数据库操作Service实现
 * @createDate 2022-05-17 17:34:59
 */
@Service
public class TopicArticleCommentStarServiceImpl extends ServiceImpl<TopicArticleCommentStarMapper,
        TopicArticleCommentStar>
        implements TopicArticleCommentStarService {

    @Autowired
    private TopicArticleCommentStarMapper starMapper;
    @Autowired
    private TopicArticleCommentMapper commentMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 点赞评论
     *
     * @param commentId
     * @return
     */
    @Override
    public R starComment(Long commentId) {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        TopicArticleCommentStar star = starMapper.selectOne(new LambdaQueryWrapper<TopicArticleCommentStar>()
                .eq(TopicArticleCommentStar::getCommentId, commentId)
                .eq(TopicArticleCommentStar::getAccountid, accountId));

        if (!ObjectUtils.isEmpty(star)) {
            LocalDateTime updateTime = star.getUpdateTime();
            star.setIsStar(star.getIsStar() == 1 ? 0 : 1);
            starMapper.updateById(star);
            if (star.getIsStar() == 0) {
                return R.success("取消点赞成功");
            }
            if ((ObjectUtils.isEmpty(updateTime) && !DateTimeUtils.isToday(star.getCreateTime())) || !DateTimeUtils.isToday(updateTime)) {
                sendMessage(commentId, accountId);
            }

        } else {
            star = new TopicArticleCommentStar(accountId, commentId);
            starMapper.insert(star);
            sendMessage(commentId, accountId);
        }
        return R.success("点赞成功");
    }

    private void sendMessage(Long commentId, String accountId) {
        TopicArticleComment comment = commentMapper.selectById(commentId);
        Long articleId = comment.getArticleId();
        String receiveAccountId = comment.getSendAccoutid();
        //判断是否给自己点赞s
        if (!accountId.equals(receiveAccountId)) {
            //封装消息对象
            UserMessage message = new UserMessage(articleId, accountId, receiveAccountId, comment.getMessage(),
                    MessageConstant.TOPIC_STAR_COMMENT_MESSAGE);
            rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE, MessageConstant.MESSAGE_INSERT_KEY,
                    message);
        }

    }
}




