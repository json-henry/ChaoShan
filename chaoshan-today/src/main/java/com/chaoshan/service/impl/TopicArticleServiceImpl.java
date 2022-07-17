package com.chaoshan.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.UserClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.*;
import com.chaoshan.mapper.TopicArticleCommentMapper;
import com.chaoshan.mapper.TopicArticleMapper;
import com.chaoshan.mapper.TopicArticleStarMapper;
import com.chaoshan.service.TopicArticleService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import com.chaoshan.utils.DateTimeUtils;
import com.chaoshan.vo.TopicArticleVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YCE
 * @description 针对表【cs_topic_article(活动文章表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:34:59
 */
@Service
public class TopicArticleServiceImpl extends ServiceImpl<TopicArticleMapper, TopicArticle>
        implements TopicArticleService {
    @Autowired
    private TopicArticleMapper articleMapper;
    @Autowired
    private TopicArticleStarMapper starMapper;
    @Autowired
    private TopicArticleCommentMapper commentMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserClient userClient;


    /**
     * 话题文章的点赞与取消点赞操作
     *
     * @param articleId
     * @return
     */
    @Override
    public R starTopicArticle(Long articleId) {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        TopicArticleStar star = starMapper.selectOne(new LambdaQueryWrapper<TopicArticleStar>()
                .eq(TopicArticleStar::getArticleId, articleId)
                .eq(TopicArticleStar::getSendAccountid, accountId));
        if (!ObjectUtils.isEmpty(star)) {
            LocalDateTime updateTime = star.getUpdateTime();
            star.setIsStar(star.getIsStar() == 1 ? 0 : 1);
            starMapper.updateById(star);
            if (star.getIsStar() == 0) {
                return R.success("取消点赞成功");
            }
            //判断上次点赞的时间是否在今天
            if ((ObjectUtils.isEmpty(updateTime) && !DateTimeUtils.isToday(star.getCreateTime())) || !DateTimeUtils.isToday(updateTime)) {
                //发送消息
                sendStarMessage(articleId, accountId);
            }
        } else {
            star = new TopicArticleStar(accountId, articleId);
            starMapper.insert(star);
            //发送消息
            sendStarMessage(articleId, accountId);
        }
        return R.success("点赞成功");
    }

    private void sendStarMessage(Long articleId, String accountId) {
        String receiveAccountId = articleMapper.selectById(articleId).getAccountid();
        //给自己点赞不发送消息
        if (!accountId.equals(receiveAccountId)) {
            //封装消息对象
            UserMessage message = new UserMessage(articleId, accountId, receiveAccountId, StrUtil.EMPTY,
                    MessageConstant.TOPIC_STAR_MESSAGE);
            rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE, MessageConstant.MESSAGE_INSERT_KEY,
                    message);
        }
    }

    /**
     * 根据文章id获取文章Vo，即提供给前端接收的对象
     *
     * @param articleId
     * @return
     */
    @Override
    @Transactional
    public TopicArticleVO getTopicArticleVOById(Long articleId) {
        TopicArticle topicArticle = articleMapper.selectById(articleId);
        if (ObjectUtils.isEmpty(topicArticle)) {
            return null;
        }
        if (ObjectUtils.isEmpty(topicArticle.getPictureLink())) {
            topicArticle.setPictureLink(null);
        }
        return getTopicArticleVoByTopicArticle(topicArticle);
    }

    /**
     * 根据话题文章再附加 点赞数、评论数、点赞状态、关注状态
     *
     * @param topicArticle
     * @return
     */
    public TopicArticleVO getTopicArticleVoByTopicArticle(TopicArticle topicArticle) {
        List<TopicArticleStar> topicArticleStars =
                starMapper.selectList(new LambdaQueryWrapper<TopicArticleStar>().eq(TopicArticleStar::getArticleId,
                        topicArticle.getId()).eq(TopicArticleStar::getIsStar, true));
        boolean isStar = false;
        boolean isFocus = false;
        long starCount = 0;
        long commentCount;
        User currentUser = LoginUser.getCurrentLoginUser();
        if (!CollectionUtils.isEmpty(topicArticleStars)) {
            starCount = topicArticleStars.size();
        }
        //判断是否处于登陆状态
        if (starCount > 0 && !ObjectUtils.isEmpty(currentUser)) {
            List<TopicArticleStar> collect =
                    topicArticleStars.stream().filter(t -> t.getSendAccountid().equals(currentUser.getAccountid()) && t.getIsStar() == 1).collect(Collectors.toList());
            //判断当前用户是否已经对该话题文章点赞
            if (!CollectionUtils.isEmpty(collect)) {
                isStar = true;
            } //查询是否已经关注
            isFocus = userClient.isFocus(topicArticle.getAccountid());
        }
        //获取话题文章用户头像
        User author = userClient.getUserById(topicArticle.getAccountid()).getData();

        commentCount =
                commentMapper.selectCount(new LambdaQueryWrapper<TopicArticleComment>()
                        .eq(TopicArticleComment::getArticleId, topicArticle.getId()));
        return new TopicArticleVO(topicArticle, starCount, commentCount, isStar, isFocus, author.getAvatar(),
                author.getUsername());
    }
}




