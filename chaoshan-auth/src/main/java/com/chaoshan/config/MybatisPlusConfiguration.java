package com.chaoshan.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @DATE: 2022/05/04 13:58
 * @Author: 小爽帅到拖网速
 */
@Configuration
@MapperScan("com.chaoshan.mapper")
public class MybatisPlusConfiguration {

    /**
     * 自动填充组件
     */
    @Component
    public class MyMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }

    // 逻辑删除组件
    @Bean
    public ISqlInjector iSqlInjector() {
        return new LogicSqlInjector();
    }
}
