package com.chaoshan.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.clients.InformationClient;
import com.chaoshan.constant.MessageConstant;
import com.chaoshan.entity.ArticleCollection;
import com.chaoshan.entity.UserMessage;
import com.chaoshan.mapper.ArticleCollectionMapper;
import com.chaoshan.mapper.UserArticleMapper;
import com.chaoshan.service.ArticleCollectionService;
import com.chaoshan.util.UtilMethod;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import com.chaoshan.util.entity.LoginUser;
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
public class ArticleCollectionServiceImpl extends ServiceImpl<ArticleCollectionMapper, ArticleCollection> implements ArticleCollectionService {

    @Autowired
    private ArticleCollectionMapper articleCollectionMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private InformationClient informationClient;
    @Autowired
    private UserArticleMapper userArticleMapper;

    /**
     * 用户收藏其他用户的文章
     *
     * @param articleCollection
     * @return
     */
    @Override
    public R collectionUserArticle(ArticleCollection articleCollection) {
        String accountid = LoginUser.getCurrentLoginUser().getAccountid();
        Integer recordCount = articleCollectionMapper.selectCount(new LambdaQueryWrapper<ArticleCollection>()
                .eq(ArticleCollection::getArticleid, articleCollection.getArticleid())
                .eq(ArticleCollection::getAccountid, accountid));
        if (recordCount == 1) {
            // 该收藏记录以存在，则为用户取消收藏
            int deleteCount = articleCollectionMapper.delete(new LambdaQueryWrapper<ArticleCollection>()
                    .eq(ArticleCollection::getAccountid, accountid).eq(ArticleCollection::getArticleid,
                            articleCollection.getArticleid()));
            if (deleteCount > 0) {
                return R.success(ResultCode.SUCCESS);
            }
        } else {
            // 该收藏记录不存在，则为用户收藏操作
            int insertCount = articleCollectionMapper.insert(articleCollection);
            if (insertCount == 1) {
                // 根据文章查询用户账号
                String accountidByArticleId = UtilMethod.getAccountidByArticleId(articleCollection.getArticleid(),
                        userArticleMapper);
                UserMessage userMessage = new UserMessage().setSendAccountid(articleCollection.getAccountid())
                        .setArticleid(articleCollection.getArticleid())
                        .setReceiveAccountid(accountidByArticleId)
                        .setMessageType(MessageConstant.ARTICLE_COLLECTION_MESSAGE);
                try {
                    informationClient.addMessage(userMessage);
                } catch (Exception e) {
                    log.error("用户用户收藏消息发送失败！");
                }
                return R.success(ResultCode.SUCCESS);
            }
        }
        return R.fail(ResultCode.FAILURE);
    }

    /**
     * 删除文章被收藏记录
     *
     * @param articleid
     */
    @Override
    public void deleteRecord(String articleid) {
        ArticleCollection articleCollection =
                articleCollectionMapper.selectOne(new LambdaQueryWrapper<ArticleCollection>().select(ArticleCollection::getId)
                .eq(ArticleCollection::getArticleid, articleid));
        if (ObjectUtil.isNotEmpty(articleCollection)) {
            Long articleCollectionId = articleCollection.getId();
            if (!(articleCollectionMapper.deleteById(articleCollectionId) == 1)) {
                log.error("删除文章收藏记录id：{}失败", articleid);
            }
        }
    }

}
