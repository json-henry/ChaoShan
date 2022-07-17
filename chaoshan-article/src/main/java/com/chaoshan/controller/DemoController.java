package com.chaoshan.controller;

import com.chaoshan.constant.RoleConstant;
import com.chaoshan.entity.ArticleTag;
import com.chaoshan.util.IpUtils;
import com.chaoshan.util.SensitiveFilter;
import com.chaoshan.util.entity.LoginUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @DATE: 2022/05/11 13:05
 * @Author: 小爽帅到拖网速
 */
@RestController
@ApiIgnore
public class DemoController {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;


  @PostMapping("/upload2")
  public int upload2(@RequestPart("files") MultipartFile[] files, @RequestPart("articleTag") ArticleTag articleTag) {
    // files.forEach(System.out::println);
    for (MultipartFile file : files) {
      // System.out.println(file.getName());
      System.out.println(file.getOriginalFilename());
    }
    System.out.println(articleTag.getTagName());
    return files.length;
  }

  @PostMapping("/upload")
  public int upload(@RequestPart("files") MultipartFile[] files, HttpServletRequest servletRequest) {
    // files.forEach(System.out::println);
    for (MultipartFile file : files) {
      // System.out.println(file.getName());
      System.out.println(file.getOriginalFilename());
    }
    System.out.println(servletRequest.getRemoteAddr().toString());
    return files.length;
  }

  @GetMapping("/demo1")
  @ApiOperation(value = "demo")
  @PreAuthorize(RoleConstant.HAS_ROLE_ADMIN)
  public String demo1() {
    return "article role_admin";
  }

  @GetMapping("/demo2")
  @ApiOperation(value = "demo")
  // @PreAuthorize(RoleConstant.Has_ROLE_TEST_USER)
  public String demo2() {
    // return JSONUtil.toBean(LoginUser.getCurrentLoginUser().toString(), User.class).getAccountid();
    return LoginUser.getCurrentLoginUser().getAccountid();
  }

  @GetMapping("/demo3")
  public String demo3() throws IOException {
    String searchkey = "你是个臭傻逼";
    //非法敏感词汇判断
    SensitiveFilter filter = SensitiveFilter.getInstance();
    int n = filter.CheckSensitiveWord(searchkey, 0, 1);
    if (n > 0) { //存在非法字符

      return "他骂你是臭傻逼！";
    }
    return "未检测到不良言语";
  }

  @GetMapping("/getIpAddress")
  public String demo4(HttpServletRequest servletRequest) {
    String ipAddr = IpUtils.getIpAddr(servletRequest);
    System.err.println(ipAddr);
    return ipAddr;
  }

  @GetMapping("/testKeyExpire")
  public String demo5() {
    // stringRedisTemplate.opsForValue().setIfAbsent("a","111", 2, TimeUnit.SECONDS);
    stringRedisTemplate.opsForHyperLogLog().add("123", "172.18.54.215");
    stringRedisTemplate.opsForHyperLogLog().add("123", "172.181.51.21");
    stringRedisTemplate.opsForHyperLogLog().add("123", "172.18.54.215");

    stringRedisTemplate.opsForHyperLogLog().add("123_copy", "172.18.54.215");
    stringRedisTemplate.opsForHyperLogLog().add("123_copy", "172.181.51.21");
    stringRedisTemplate.opsForHyperLogLog().add("123_copy", "172.18.54.215");
    stringRedisTemplate.expire("123", 2, TimeUnit.SECONDS);
    return "demo5 success!";
  }
}
