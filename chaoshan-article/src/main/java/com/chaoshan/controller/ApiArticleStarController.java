package com.chaoshan.controller;

import com.chaoshan.entity.ArticleCommentReplyStar;
import com.chaoshan.entity.ArticleStar;
import com.chaoshan.service.ArticleCommentReplyStarService;
import com.chaoshan.service.ArticleCommentStarService;
import com.chaoshan.service.ArticleStarService;
import com.chaoshan.util.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @DATE: 2022/05/19 21:10
 * @Author: 小爽帅到拖网速
 */
@RestController
@Api(value = "文章点赞模块")
@RequestMapping("/api")
// @ApiIgnore
public class ApiArticleStarController {

  @Autowired
  private ArticleStarService articleStarService;
  @Autowired
  private ArticleCommentStarService articleCommentStarService;
  @Autowired
  private ArticleCommentReplyStarService articleCommentReplyStarService;

  @PostMapping("/starUserArticle")
  @ApiOperation(value = "文章点赞操作", notes = "取反操作，需要参数accountid:账户id,articleid:文章id")
  // @ApiImplicitParam(name = "articleStar", value = "", paramType = "body")
  public R starArticleOper(@RequestBody ArticleStar articleStar) {
    return articleStarService.starArticleOper(articleStar);
  }

  /*@PostMapping("/starArticleCommentOper")
  @ApiOperation(value = "文章评论点赞操作", notes = "commentId评论id")
  // @ApiImplicitParam(name = "articleCommentStar", value = "需要参数articleid:文章id,commentId:评论id", paramType = "body")
  public R starArticleCommentOper(@RequestBody ArticleCommentStar articleCommentStar) {
    return articleCommentStarService.starArticleCommentOper(articleCommentStar);
  }*/

  @GetMapping("/starArticleCommentOper/{commentId}")
  @ApiOperation(value = "文章评论点赞操作", notes = "commentId评论id")
  public R startArticle(@PathVariable Long commentId) {
    return articleCommentStarService.starArticleCommentOper(commentId);
  }

  @PostMapping("/starArticleCommentReplyOper")
  @ApiOperation(value = "文章评论回复点赞操作", notes = "用户对文章评论回复的点赞操作，根据请求会判断当前是否点赞，取反操作")
  public R starArticleCommentReplyOper(@RequestBody ArticleCommentReplyStar articleCommentReplyStar) {
    return articleCommentReplyStarService.starArticleCommentReplyOper(articleCommentReplyStar);
  }
}
