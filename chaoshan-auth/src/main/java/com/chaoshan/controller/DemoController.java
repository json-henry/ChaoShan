package com.chaoshan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @DATE: 2022/04/28 13:31
 * @Author: 小爽帅到拖网速
 */
@RestController
@Api("demo-controller")
public class DemoController {

    @Value("${server.port}")
    String port;


    @GetMapping("/hello")
    @ApiOperation("demo")
    public String demo() {
        return "当前端口号为：" + port;
    }
}
