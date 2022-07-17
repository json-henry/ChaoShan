package com.chaoshan.config;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.Permission;
import com.chaoshan.entity.User;
import com.chaoshan.mapper.IUserDao;
import com.chaoshan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @DATE: 2022/04/28 14:03
 * @Author: 小爽帅到拖网速
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private IUserDao userDao;
  @Autowired
  private IUserService iUserService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  //认证管理器
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/login",
                    "/logout",
                    "/css/**",
                    "/js/**",
                    "/index.html",
                    "favicon.ico",
                    "/doc.html",
                    "/webjars/**",
                    "/swagger-resources/**",
                    "/v2/api-docs/**",
                    "/captcha",
                    "/user/register",
                    "/ws/**"
            ).permitAll()
            .anyRequest().permitAll()
            .and()
            .formLogin();
  }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    return (accountId) -> {
      User userDb = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccountid, accountId));
      if (ObjectUtils.isEmpty(userDb)) {
        // 数据库没有用户数据，首次登录即注册
        userDb = iUserService.registerUserOfLogin(accountId, passwordEncoder());
      }
      List<String> permissionCodeList =
              userDao.findUserPermission(userDb.getId()).stream().map(Permission::getCode).collect(Collectors.toList());
      String[] strings = new String[permissionCodeList.size()];
      permissionCodeList.toArray(strings);

      String jsonStr = JSONUtil.toJsonStr(userDb);
      UserDetails userDetails =
              org.springframework.security.core.userdetails.User.withUsername(jsonStr).password(userDb.getPassword())
              .authorities(strings).build();
      return userDetails;
    };
  }
}
