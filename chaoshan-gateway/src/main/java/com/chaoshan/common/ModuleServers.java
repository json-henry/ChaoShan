package com.chaoshan.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @DATE: 2022/05/11 09:29
 * @Author: 小爽帅到拖网速
 */
@Configuration
@ConfigurationProperties(prefix = "chaoshan")
public class ModuleServers {
    private static List<String> modules = new ArrayList<>();

    public static List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }
}
