package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.UserClient;
import com.chaoshan.entity.TopicArticleComment;
import com.chaoshan.entity.TopicArticleCommentStar;
import com.chaoshan.entity.TopicArticleReply;
import com.chaoshan.entity.User;
import com.chaoshan.mapper.TopicArticleCommentMapper;
import com.chaoshan.mapper.TopicArticleCommentStarMapper;
import com.chaoshan.mapper.TopicArticleReplyMapper;
import com.chaoshan.service.TopicArticleCommentService;
import com.chaoshan.util.entity.LoginUser;
import com.chaoshan.vo.TopicArticleCommentVO;
import com.chaoshan.vo.TopicArticleReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YCE
 * @description 针对表【cs_topic_article_comment(话题文章评论表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:34:59
 */
@Service
public class TopicArticleCommentServiceImpl extends ServiceImpl<TopicArticleCommentMapper, TopicArticleComment>
        implements TopicArticleCommentService {

    @Autowired
    private TopicArticleCommentMapper commentMapper;
    @Autowired
    private TopicArticleCommentStarMapper starMapper;
    @Autowired
    private TopicArticleReplyMapper replyMapper;
    @Autowired
    private TopicArticleReplyServiceImpl replyService;
    @Autowired
    private UserClient userClient;

    /**
     * 根据话题文章id获取文章的所有评论，并携带每条评论的点赞数和回复数
     *
     * @param articleId
     * @return
     */
    @Override
    @Transactional
    public List<TopicArticleCommentVO> getArticleCommentVOList(Long articleId) {
        List<TopicArticleCommentVO> results = new ArrayList<>();
        List<TopicArticleComment> commentList =
                commentMapper.selectList(new LambdaQueryWrapper<TopicArticleComment>().eq(TopicArticleComment::getArticleId,
                        articleId).orderByDesc(TopicArticleComment::getCreateTime));
        for (TopicArticleComment comment : commentList) {
            List<TopicArticleCommentStar> stars =
                    starMapper.selectList(new LambdaQueryWrapper<TopicArticleCommentStar>()
                            .eq(TopicArticleCommentStar::getCommentId, comment.getId())
                            .eq(TopicArticleCommentStar::getIsStar, true));
            boolean isStar = false;
            User loginUser = LoginUser.getCurrentLoginUser();
            Integer starNum = 0;
            if (!CollectionUtils.isEmpty(stars)) {
                starNum = stars.size();
            }
            if (starNum > 0 && !ObjectUtils.isEmpty(loginUser)) {
                List<TopicArticleCommentStar> collect =
                        stars.stream().filter(s -> s.getAccountid().equals(loginUser.getAccountid()) && s.getIsStar() == 1).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(collect)) {
                    isStar = true;
                }
            }
            User author = userClient.getUserById(comment.getSendAccoutid()).getData();
            Integer replyNum =
                    replyMapper.selectCount(new LambdaQueryWrapper<TopicArticleReply>().eq(TopicArticleReply::getArticleCommentId, comment.getId()));

            //获取评论下的所有回复
            List<TopicArticleReplyVO> replyVOS =
                    replyService.getTopicArticleReplyVOByCommentId(comment.getId()).getData();

            results.add(new TopicArticleCommentVO(comment, starNum, replyNum, author.getAvatar(), author.getUsername(),
                    isStar, replyVOS));
        }
        return results;
    }
}