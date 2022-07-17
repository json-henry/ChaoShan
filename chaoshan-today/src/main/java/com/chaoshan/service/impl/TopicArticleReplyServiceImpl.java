package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.UserClient;
import com.chaoshan.entity.TopicArticleReply;
import com.chaoshan.entity.TopicArticleReplyStar;
import com.chaoshan.entity.User;
import com.chaoshan.mapper.TopicArticleReplyMapper;
import com.chaoshan.service.TopicArticleReplyService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import com.chaoshan.vo.TopicArticleReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YCE
 * @description 针对表【cs_topic_article_reply(话题下的文章评论的回复表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:34:59
 */
@Service
public class TopicArticleReplyServiceImpl extends ServiceImpl<TopicArticleReplyMapper, TopicArticleReply>
        implements TopicArticleReplyService {
    @Autowired
    private TopicArticleReplyServiceImpl replyService;
    @Autowired
    private TopicArticleReplyStarServiceImpl starService;
    @Autowired
    private UserClient userClient;

    @Override
    public R<List<TopicArticleReplyVO>> getTopicArticleReplyVOByCommentId(Long commentId) {
        if (ObjectUtils.isEmpty(commentId)) {
            return null;
        }
        List<TopicArticleReplyVO> replyVOS = new ArrayList<>();
        List<TopicArticleReply> replyList =
                replyService.list(new LambdaQueryWrapper<TopicArticleReply>().eq(TopicArticleReply::getArticleCommentId,
                        commentId).orderByAsc(TopicArticleReply::getCreateTime));
        for (TopicArticleReply reply : replyList) {
            List<TopicArticleReplyStar> stars =
                    starService.list(new LambdaQueryWrapper<TopicArticleReplyStar>()
                            .eq(TopicArticleReplyStar::getReplyId, reply.getId())
                            .eq(TopicArticleReplyStar::getIsStar, true));
            Integer starNum = 0;
            User loginUser = LoginUser.getCurrentLoginUser();
            boolean isMeStar = false;
            if (!CollectionUtils.isEmpty(stars)) {
                starNum = stars.size();
            }
            //有登陆的情况下
            if (starNum > 0 && !ObjectUtils.isEmpty(loginUser)) {
                List<TopicArticleReplyStar> collect =
                        stars.stream().filter(s -> s.getAccountid().equals(loginUser.getAccountid()) && s.getIsStar() == 1).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(collect)) {
                    isMeStar = true;
                }
            }
            //获取回复的用户信息
            User user = userClient.getUserById(reply.getAccountid()).getData();
            replyVOS.add(new TopicArticleReplyVO(reply, starNum, user.getAvatar(), user.getUsername(), isMeStar));
        }
        return R.data(replyVOS);
    }
}




