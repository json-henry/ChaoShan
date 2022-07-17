package com.chaoshan;

import com.chaoshan.config.DefaultFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @DATE: 2022/05/05 23:33
 * @Author: 小爽帅到拖网速
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching  // 开启Redis缓存注解
@EnableFeignClients(basePackages = "com.chaoshan.clients", defaultConfiguration = DefaultFeignConfiguration.class)
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }
}
