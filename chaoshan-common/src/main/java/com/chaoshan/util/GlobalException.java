package com.chaoshan.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * @DATE: 2022/05/04 13:49
 * @Author: 小爽帅到拖网速
 */
@RestControllerAdvice
@ConditionalOnWebApplication(type = SERVLET)
public class GlobalException {
  /*@ExceptionHandler(SQLException.class)
  public R mySqlException(SQLException e){
    // sql违反了完整性约束异常
    if (e instanceof SQLIntegrityConstraintViolationException){
      return R.fail("该数据有关联数据，操作失败！");
    }
    return R.fail("数据库异常，操作失败！");
  }*/

  /*@ExceptionHandler(RuntimeException.class)
  public R myRuninngException(RuntimeException e){
    System.out.println(e.getMessage());
    return R.fail("系统出现错误-------------------");
  }*/
}
