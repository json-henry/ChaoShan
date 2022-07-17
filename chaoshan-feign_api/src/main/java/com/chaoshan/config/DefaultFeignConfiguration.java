package com.chaoshan.config;

import com.chaoshan.clients.fallback.UserClientFallbackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @DATE: 2022/05/12 09:53
 * @Author: 小爽帅到拖网速
 */
public class DefaultFeignConfiguration {

    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public UserClientFallbackFactory userClientFallbackFactory() {
        return new UserClientFallbackFactory();
    }
}
