package com.chaoshan.clients.fallback;


import com.chaoshan.clients.UserClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @DATE: 2022/05/12 09:53
 * @Author: 小爽帅到拖网速
 */
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

  private static final Logger log = LoggerFactory.getLogger(UserClientFallbackFactory.class);

  @Override
  public UserClient create(Throwable throwable) {
    return null;
  }
}
