package com.chaoshan.controller;

import cn.hutool.core.util.ObjectUtil;
import com.chaoshan.entity.ArticleComment;
import com.chaoshan.entity.ArticleReply;
import com.chaoshan.service.ArticleCommentService;
import com.chaoshan.service.ArticleReplyService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @DATE: 2022/05/17 21:39
 * @Author: 小爽帅到拖网速
 */
@RestController
@RequestMapping("/api")
@Api(value = "文章评论模块")
public class ApiArticleCommentController {
  @Autowired
  private ArticleCommentService articleCommentService;
  @Autowired
  private ArticleReplyService articleReplyService;

  @PutMapping("/commentArticle")
  @ApiOperation(value = "评论文章", notes = "实体中包括主动评论的用户id，文章id，评论内容")
  public R commentArticle(@RequestBody ArticleComment articleComment) {
    if (ObjectUtil.hasEmpty(articleComment.getArticleid(), articleComment.getMessage())) {
      return R.fail(ResultCode.PARAM_MISS, "文章id或者评论内容不能为空");
    }
    return articleCommentService.commentArticle(articleComment);
  }

  @DeleteMapping("/deleteCommentArticle/{commentId}")
  @ApiOperation(value = "删除文章评论记录", notes = "根据评论id删除文章评论记录")
  public R deleteCommentArticle(@PathVariable Long commentId) {
    if (ObjectUtil.isEmpty(commentId)) {
      return R.fail(ResultCode.PARAM_MISS, "评论id不能为空");
    }
    return articleCommentService.deleteCommentArticle(commentId);
  }

  @PutMapping("/replyCommentArticle")
  @ApiOperation(value = "回复评论文章", notes = "实体中包括评论id,目标用户id，评论内容")
  public R replyCommentArticle(@RequestBody ArticleReply articleReply) {
    if (ObjectUtil.hasEmpty(articleReply.getToAccountid(),
            articleReply.getCommentId(), articleReply.getContent())) {
      return R.fail(ResultCode.PARAM_VALID_ERROR);
    }
    return articleReplyService.replyCommentArticle(articleReply);
  }

  @DeleteMapping("/deletereplyCommentArticle/{commentId}")
  @ApiOperation(value = "删除文章回复评论记录", notes = "根据评论id删除文章评论记录")
  public R deleteReplyCommentArticle(@PathVariable Long commentId) {
    if (ObjectUtil.isEmpty(commentId)) {
      return R.fail(ResultCode.PARAM_MISS, "评论id不能为空");
    }
    return articleReplyService.deleteReplyCommentArticle(commentId);
  }
}
