package com.chaoshan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @DATE: 2022/04/28 17:47
 * @Author: 小爽帅到拖网速
 */
@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

    // 这个资源服务id跟授权服务器配置的资源名是一致的
    public static final String RESOURCE_ID = "res1";

    // jwt令牌校验方式
    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)//资源 id
                .tokenStore(tokenStore)   // 采用jwt的令牌
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers(
                        "/api/getUserById/**", "/api/getArticleInfo/**", "/api/postPhoto/**", "/api/postManyFile/**",
                        "/v2/**",//swagger api json
                        "/css/**", "/js/**", "/images/**", "/webjars/**", "**/favicon.ico", "/index",
                        "/swagger-resources/configuration/ui",//用来获取支持的动作
                        "/swagger-resources",//用来获取api-docs的URI
                        "/swagger-resources/configuration/security",//安全选项
                        "/swagger-ui.html", "/doc.html").permitAll()
                .antMatchers("/**").access("#oauth2.hasScope('ROLE_USER')")
                .and().csrf().disable()
                // 基于token，所以要关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
