package com.chaoshan.filter;

import com.alibaba.fastjson.JSON;
import com.chaoshan.common.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DATE: 2022/04/28 21:37
 * @Author: 小爽帅到拖网速
 */
@Slf4j
public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        //从安全上下文中拿 到用户身份对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }
        log.info("请求路径：{}", ctx.getRequest().getRequestURI());
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        //取出用户身份信息
        String principal = userAuthentication.getName();

        //取出用户权限
        List<String> authorities = new ArrayList<>();
        //从userAuthentication取出权限，放在authorities
        userAuthentication.getAuthorities().stream().forEach(c -> authorities.add(((GrantedAuthority) c).getAuthority()));

        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String, Object> jsonToken = new HashMap<>(requestParameters);
        if (userAuthentication != null) {
            jsonToken.put("principal", principal);
            jsonToken.put("authorities", authorities);
        }

        //把身份信息和权限信息放在json中，加入http的header中,转发给微服务
        ctx.addZuulRequestHeader("json-token", EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));

        return null;
    }
}

