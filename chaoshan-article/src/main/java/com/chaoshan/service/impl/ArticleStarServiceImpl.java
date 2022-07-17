package com.chaoshan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.InformationClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.ArticleStar;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.ArticleStarMapper;
import com.chaoshan.mapper.UserArticleMapper;
import com.chaoshan.service.ArticleStarService;
import com.chaoshan.util.UtilMethod;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@Service
@Slf4j
public class ArticleStarServiceImpl extends ServiceImpl<ArticleStarMapper, ArticleStar> implements ArticleStarService {

    @Autowired
    private ArticleStarMapper articleStarMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private InformationClient informationClient;
    @Autowired
    private UserArticleMapper userArticleMapper;

    /**
     * 用户对文章点赞操作，根据请求会去判断当前是否点赞，取反操作
     *
     * @param articleStar
     * @return
     */
    @Override
    public R starArticleOper(ArticleStar articleStar) {

        if (ObjectUtil.hasEmpty(articleStar.getArticleid(), articleStar.getAccountid())) {
            return R.fail(ResultCode.PARAM_MISS);
        }
        // 先判断是否有记录，如果有则为取消点赞操作，如果没有则为点赞操作
        Integer count = articleStarMapper.selectCount(new LambdaQueryWrapper<ArticleStar>()
                .eq(ArticleStar::getArticleid, articleStar.getArticleid()).eq(ArticleStar::getAccountid,
                        articleStar.getAccountid()));
        if (count > 0) {
            // 用户取消点赞操作
            articleStarMapper.delete(new LambdaQueryWrapper<ArticleStar>()
                    .eq(ArticleStar::getArticleid, articleStar.getArticleid()).eq(ArticleStar::getAccountid,
                            articleStar.getAccountid()));
        } else {
            // 用户点赞操作
            int insertCount = articleStarMapper.insert(articleStar);
            if (insertCount == 0) {
                return R.success(ResultCode.FAILURE);
            }
            int recordCount = articleStarMapper.selectRecently(articleStar.getArticleid(), articleStar.getAccountid());
            // 如果是24小时之内只有一条相同的记录则插入消息标
            if (recordCount == 1) {
                log.info("插入消息");
                String accountidByArticleId = UtilMethod.getAccountidByArticleId(articleStar.getArticleid(),
                        userArticleMapper);
                UserMessage userMessage = new UserMessage()
                        .setSendAccountid(articleStar.getAccountid())
                        .setReceiveAccountid(accountidByArticleId)
                        .setArticleid(articleStar.getArticleid())
                        .setMessageType(MessageConstant.ARTICLE_STAR_MESSAGE);
                // rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE,ARTICLE_MESSAGE_INSERT_ROUTING, JSONUtil.toJsonStr(userMessage));
                informationClient.addMessage(userMessage);

            }
        }
        return R.success(ResultCode.SUCCESS);
    }
}
