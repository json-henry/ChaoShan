package com.chaoshan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @DATE: 2022/04/28 13:32
 * @Author: 小爽帅到拖网速
 */
@SpringBootApplication
@MapperScan("com.chaoshan.mapper")
public class AuthApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
  }
}
