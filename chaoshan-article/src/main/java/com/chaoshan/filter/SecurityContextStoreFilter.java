/*
package com.chaoshan.filter;

import cn.hutool.core.thread.ThreadUtil;
import com.chaoshan.common.ThreadLocalUtil;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

*/
/**
 * @DATE: 2022/05/15 18:12
 * @Author: 小爽帅到拖网速
 *//*

@Component
@Order(-1)
public class SecurityContextStoreFilter implements Filter {
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
  throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    if (authentication!=null&&authentication.isAuthenticated()&&authentication instanceof OAuth2Authentication){
      ThreadLocalUtil.removeThreadStore();
      ThreadLocalUtil.setAuthenticationThreadLocal(authentication);
    }
    filterChain.doFilter(servletRequest,servletResponse);
  }
}
*/
