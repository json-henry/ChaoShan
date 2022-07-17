package com.chaoshan.service.search.impl;

import com.chaoshan.clients.InformationClient;
import com.chaoshan.service.ArticleService;
import com.chaoshan.service.search.ArticleIndexService;
import com.chaoshan.service.search.RabbitmqListenSerive;
import com.chaoshan.service.search.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.chaoshan.common.constant.MqConstants.*;

/**
 * @DATE: 2022/05/17 15:55
 * @Author: 小爽帅到拖网速
 */
@Service
@Slf4j
public class RabbitmqListenSeriveImpl implements RabbitmqListenSerive {

    @Autowired
    private ArticleIndexService articleIndexService;
    @Autowired
    private InformationClient informationClient;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ArticleService articleService;


    /**
     * 监听文章数据新增
     *
     * @param accountids
     */
    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ARTICLE_INSERT_QUEUE),
            exchange = @Exchange(name = ARTICLE_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = ARTICLE_INSERT_ROUTING
    ))
    public void articleInsert(String accountids) {
        articleIndexService.fillArticleData(accountids);
    }

    /**
     * 监听文章数据删除
     *
     * @param articleid
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ARTICLE_DELETE_QUEUE),
            exchange = @Exchange(name = ARTICLE_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = ARTICLE_DELETE_ROUTING
    ))
    @Override
    public void articleDelete(String articleid) {
        // 删除索引库中的数据
        if ((!articleIndexService.deleteArticleData(articleid).isSuccess())) {
            log.error("索引库中删除文章{}失败", articleid);
        }
        // 删除点赞表，评论表，回复表，回复点赞表，收藏表中的数据
        articleService.deleteRelatedData(articleid);
    }

    /**
     * 监听文章数据更新
     *
     * @param accountid
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ARTICLE_UPDATE_QUEUE),
            exchange = @Exchange(name = ARTICLE_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = ARTICLE_UPDATE_ROUTING
    ))
    @Override
    public void articleUpdate(String accountid) {
        articleIndexService.updateArticleData(accountid);
    }

    // /**
    //  * 监听消息插入
    //  * @param messageJsonStr
    //  */
    // @Override
    // @RabbitListener(bindings = @QueueBinding(
    //         value = @Queue(name = ARTICLE_MESSAGE_QUEUE),
    //         exchange = @Exchange(name = ARTICLE_EXCHANGE, type = ExchangeTypes.DIRECT),
    //         key = ARTICLE_MESSAGE_INSERT_ROUTING
    // ))
    // public void articleMessageInsert(String messageJsonStr) {
    //   UserMessage userMessage = JSONUtil.toBean(messageJsonStr, UserMessage.class);
    //   // 插入消息
    //   log.info("监听消息插入");
    //   informationClient.addMessage(userMessage);
    // }

    /**
     * 监听新增热搜词
     *
     * @param keyword
     */
    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ARTICLE_HOT_SEARCH_QUEUE),
            exchange = @Exchange(name = ARTICLE_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = ARTICLE_HOT_SEARCH_ROUTING
    ))
    public void insertHotSearch(String keyword) {
        redisService.incrementScore(keyword);
    }

    /**
     * 监听文章浏览量自增
     *
     * @param mapStr
     */
    @Override
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ARTICLE_PAGEVIEW_QUEUE),
            exchange = @Exchange(name = ARTICLE_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = ARTICLE_PAGEVIEW_ROUTING
    ))
    public void addArticlePageview(String mapStr) {
        redisService.articlePageviewIncrement(mapStr);
    }
}
