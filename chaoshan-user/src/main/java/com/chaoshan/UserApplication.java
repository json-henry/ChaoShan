package com.chaoshan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @DATE: 2022/05/05 23:33
 * @Author: 小爽帅到拖网速
 */
@SpringBootApplication
@EnableCaching  // 开启Redis缓存注解
@EnableFeignClients //开启feign支持
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
