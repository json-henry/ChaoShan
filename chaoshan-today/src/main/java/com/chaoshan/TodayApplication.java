package com.chaoshan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-12  19:35
 * @Description: 今日潮汕服务主启动类
 * @Version: 1.0
 */
@SpringBootApplication
@EnableFeignClients
public class TodayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodayApplication.class, args);
    }
}
