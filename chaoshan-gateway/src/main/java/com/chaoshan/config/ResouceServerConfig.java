package com.chaoshan.config;

import com.chaoshan.common.ModuleServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;


@Configuration
public class ResouceServerConfig {

  public static final String RESOURCE_ID = "res1";

  @Configuration
  @EnableResourceServer
  public class AuthServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
      resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
              .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
              .antMatchers("/auth/**", "/article/**", "/user/**", "/information/**", "/today/**").permitAll();
    }
  }


  @Configuration
  @EnableResourceServer
  public class DemoServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
      resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
              .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      for (String module : ModuleServers.getModules()) {
        http.authorizeRequests().antMatchers("/" + module + "/v2/api-docs",
                        "/" + module + "/doc.html", "/" + module + "/webjars/**",
                        "/" + module + "/swagger-resources/**").permitAll()
                .antMatchers("/" + module + "/**").access("#oauth2.hasScope('ROLE_API')");
      }
      // http.authorizeRequests().antMatchers("/article/api/**").permitAll();
    /*  http.authorizeRequests()
              .antMatchers("/demo/v2/api-docs","/demo/doc.html","/demo/webjars/**",
                      "/demo/swagger-resources/**",
                      "/user/v2/api-docs","/user/doc.html","/user/webjars/**",
                      "/user/swagger-resources/**").permitAll()
              .antMatchers("/demo/**","/user/**").access("#oauth2.hasScope('ROLE_API')");*/
    }
  }

}
