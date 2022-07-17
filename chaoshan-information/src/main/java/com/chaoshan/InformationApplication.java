package com.chaoshan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-12  18:09
 * @Description: 消息服务主启动类
 * @Version: 1.0
 */

@SpringBootApplication
@EnableFeignClients
public class InformationApplication {

    public static void main(String[] args) {
        SpringApplication.run(InformationApplication.class, args);
    }
}
