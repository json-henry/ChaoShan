package com.chaoshan.controller;

import com.chaoshan.service.ArticleService;
import com.chaoshan.service.search.RedisService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import com.chaoshan.util.api.Rpage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @DATE: 2022/05/11 16:27
 * @Author: 小爽帅到拖网速
 */
@RestController
@RequestMapping("/system")
@Api(value = "后台模块")
public class SystemArticleController {

  @Autowired
  private ArticleService articleService;
  @Autowired
  private RedisService redisService;

  @GetMapping("/getArtPage")
  @ApiOperation(value = "根据类型返回分页文章记录")
  @ApiImplicitParams(value = {
          @ApiImplicitParam(name = "currentPage", value = "当前页,默认值为1", required = false, paramType = "query"),
          @ApiImplicitParam(name = "size", value = "记录数,默认为10", required = false, paramType = "query"),
          @ApiImplicitParam(name = "type", value = "类型 1饮食文化 2名人景点 3民间艺术 4潮玩攻略", paramType = "query")
  })
  public R<Rpage> getArtPage(@RequestParam(defaultValue = "1") Integer currentPage,
                             @RequestParam(defaultValue = "10") Integer size,
                             @RequestParam(defaultValue = "1") Integer type) {
    return articleService.getArtPage(currentPage, size, type);
  }

  @GetMapping("/examineArticle")
  @ApiOperation(value = "根据文章id修改文章审核状态")
  public R examineArticle(String[] accountids) {
    if (!(accountids.length > 0)) {
      return R.fail(ResultCode.PARAM_VALID_ERROR, "文章id不能为空");
    }
    return articleService.examineArticle(accountids);
  }

  @PostMapping("/addHotSearchKeyword")
  @ApiOperation(value = "添加热搜词")
  @ApiImplicitParam(name = "keywords", value = "热搜关键字组", paramType = "query")
  public R addHotSearchKeyword(@RequestParam String[] keywords) {
    if (!(keywords.length > 0)) {
      return R.fail(ResultCode.PARAM_MISS);
    }
    return redisService.addHotSearchKeyword(keywords);
  }
}
