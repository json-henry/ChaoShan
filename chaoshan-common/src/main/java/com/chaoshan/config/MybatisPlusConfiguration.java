package com.chaoshan.config;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.chaoshan.entity.User;
import com.chaoshan.util.entity.LoginUser;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
public class MybatisPlusConfiguration {
    /**
     * 分页插件
     *
     * @return
     */
    // 分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 自动填充组件
     */
    @Component
    public class MyMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
            User currentLoginUser = LoginUser.getCurrentLoginUser();
            if (!ObjectUtil.isEmpty(currentLoginUser)) {
                this.setFieldValByName("createBy", currentLoginUser.getAccountid(), metaObject);
            }
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
            User currentLoginUser = LoginUser.getCurrentLoginUser();
            if (!ObjectUtil.isEmpty(currentLoginUser)) {
                this.setFieldValByName("updateBy", currentLoginUser.getAccountid(), metaObject);
            }
        }
    }

    // 逻辑删除组件
    @Bean
    public ISqlInjector iSqlInjector() {
        return new LogicSqlInjector();
    }
}
