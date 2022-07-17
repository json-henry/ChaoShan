package com.chaoshan.common;

import com.chaoshan.common.entity.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @DATE: 2022/05/05 09:41
 * @Author: 小爽帅到拖网速
 */
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerServers {
    private static List<Server> servers = new ArrayList<>();

    public static List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }
}
