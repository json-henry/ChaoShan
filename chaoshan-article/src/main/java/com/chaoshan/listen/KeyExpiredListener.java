package com.chaoshan.listen;

import com.chaoshan.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import static com.chaoshan.common.constant.ArticleRedisConstant.ARTICLE_ID_KEY;
import static com.chaoshan.common.constant.ArticleRedisConstant.PREFIX;

/**
 * @DATE: 2022/05/19 14:43
 * @Author: 小爽帅到拖网速
 */
@Component
@Slf4j
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;
  @Autowired
  private ArticleService articleService;

  public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
    super(listenerContainer);
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String expirekey = message.toString();
    Long size = stringRedisTemplate.opsForHyperLogLog().size(PREFIX + expirekey);
    stringRedisTemplate.delete(PREFIX + expirekey);
    log.info("监听过期key：{}", expirekey);
    if (expirekey.startsWith(ARTICLE_ID_KEY)) {
      String articleid = expirekey.substring(ARTICLE_ID_KEY.length());
      articleService.updateArticlePageview(articleid, size);
    }
  }
}
