package com.chaoshan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/**").permitAll();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(
            "/article/api/**", "/v2/api-docs",//swagger api json
            "/webjars/**",
            "/swagger-resources/configuration/ui",//用来获取支持的动作
            "/swagger-resources",//用来获取api-docs的URI
            "/swagger-resources/configuration/security",//安全选项
            "/swagger-ui.html", "/doc.html");
  }
}
