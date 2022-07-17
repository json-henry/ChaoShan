package com.chaoshan.controller;

import cn.hutool.core.util.ObjectUtil;
import com.chaoshan.entity.dto.ArticleSearchDTO;
import com.chaoshan.entity.dto.ArticleTypeListDTO;
import com.chaoshan.service.ArticleService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @DATE: 2022/05/11 22:46
 * @Author: 小爽帅到拖网速
 */
@RestController
@RequestMapping("/api")
public class ApiArticleController {

  @Autowired
  private ArticleService articleService;

  @GetMapping("/shufflingfigure")
  @ApiOperation(value = "轮播图")
  public R<List<ArticleSearchDTO>> shufflingfigure() throws IOException {
    return articleService.shufflingfigure();
  }

  @GetMapping("/recommendationDaily")
  @ApiOperation(value = "首页每日推荐", notes = "开放接口:返回4个模块中综合排序前8个作为每日推荐")
  public R<List<ArticleSearchDTO>> recommendationDaily() {
    return articleService.recommendationDaily();
  }

  @GetMapping("/getArticleListByType/{type}/{count}")
  @ApiOperation(value = "根据类型返回文章列表", notes = "开放接口:类型：1(名人景点) 2(饮食文化) 3(民间艺术) 4(潮玩攻略)")
  @ApiImplicitParams(
          value = {
                  @ApiImplicitParam(name = "type", value = "类型", required = true, paramType = "path"),
                  @ApiImplicitParam(name = "count", value = "记录数", required = true, paramType = "path")
          })
  public R<ArticleTypeListDTO> getArticleListByType(@PathVariable Integer type, @PathVariable Integer count) {
    if (type < 0 || type > 4) {
      return R.fail(ResultCode.PARAM_VALID_ERROR, "type大于4或小于0");
    }
    return articleService.getArticleListByType(type, count);
  }

  @GetMapping("/getLikeCollectStatus/{articleId}")
  @ApiOperation(value = "根据文章id判断当前登录用户是否点赞收藏")
  public R<Map<String, String>> getLikeCollectStatus(@PathVariable String articleId) {
    if (ObjectUtil.isEmpty(articleId)) {
      return R.fail(ResultCode.FAILURE, "文章id不能为空");
    }
    return articleService.getLikeCollectStatus(articleId);

  }


}
