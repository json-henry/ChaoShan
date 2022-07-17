package com.chaoshan.controller.feignInvoke;

import cn.hutool.core.util.ObjectUtil;
import com.chaoshan.entity.Article;
import com.chaoshan.entity.ArticleCollection;
import com.chaoshan.service.*;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @DATE: 2022/05/15 11:42
 * @Author: 小爽帅到拖网速
 */
@RestController
@RequestMapping("/api")
// @ApiIgnore
public class ApiArticleFeignController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleStarService articleStarService;
    @Autowired
    private ArticleCommentStarService articleCommentStarService;
    @Autowired
    private ArticleCommentReplyStarService articleCommentReplyStarService;
    @Autowired
    private ArticleCollectionService articleCollectionService;

    /**
     * 根据文章id返回文章详情
     *
     * @param articleid
     * @return 文章详情内容
     */
    @GetMapping("/getArticleDetail/{articleid}")
    @ApiOperation("根据文章id返回文章详情")
    @ApiImplicitParam(name = "articleid", value = "文章id", required = true, paramType = "path")
    public R<Article> getArticleDetail(@PathVariable("articleid") Integer articleid, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(articleid)) {
            return R.fail("id不可为空");
        }
        return articleService.getArticleById(articleid, request);
    }

    /**
     * 用户查询已发布的文章,包括封面，标题，点赞数，收藏数，时间
     *
     * @param accountid
     * @return 用户自作文章列表
     */
    @GetMapping("userPubilshedArticle/{accountid}/{keyword}")
    @ApiOperation(hidden = true, value = "用户查看文章列表", notes = "用户查询已发布的文章,包括封面，标题，点赞数，收藏数，时间")
    @ApiImplicitParam(name = "accountid", value = "用户账号", paramType = "path")
    public R<List<Article>> getArticleList(@PathVariable("accountid") String accountid, String keyword) {
        if (!StringUtils.hasText(accountid)) {
            return R.fail(ResultCode.PARAM_VALID_ERROR, "accountid不能为空");
        }
        return articleService.userPubilshedArticle(accountid, keyword);
    }

    /**
     * @param accountid
     * @return 用户收藏的文章列表
     */
    @GetMapping("userCollectionArticles/{accountid}")
    @ApiOperation(value = "用户收藏的文字列表", hidden = true)
    @ApiImplicitParam(name = "accountid", value = "用户账号", paramType = "path")
    public R<List<Article>> getCollectionList(@PathVariable("accountid") String accountid, @RequestParam(required =
            false) String keyword) {
        if (!StringUtils.hasText(accountid)) {
            return R.fail(ResultCode.PARAM_VALID_ERROR, "accountid不能为空");
        }
        return articleService.getUserCollectionArticles(accountid, keyword);
    }

    /**
     * @param accountid
     * @return 用户点赞的文章列表
     */
    @GetMapping("userStarArticles/{accountid}")
    @ApiOperation(value = "用户点赞的文章列表", hidden = true)
    @ApiImplicitParam(name = "accountid", value = "用户账号", paramType = "path")
    public R<List<Article>> getStarList(@PathVariable("accountid") String accountid, @RequestParam String keyword) {
        if (!StringUtils.hasText(accountid)) {
            return R.fail(ResultCode.PARAM_VALID_ERROR, "accountid不能为空");
        }
        return articleService.userStarArticles(accountid, keyword);
    }

    /**
     * @param accountid
     * @return 用户草稿箱文章列表
     */
    @GetMapping("/draftsArticles/{accountid}")
    @ApiOperation("用户草稿箱文章列表")
    @ApiImplicitParam(name = "accountid", value = "用户账号", paramType = "path")
    public R<List<Article>> getDrafts(@PathVariable("accountid") String accountid,
                                      @RequestParam(value = "keyword", required = false) String keyword) {
        if (!StringUtils.hasText(accountid)) {
            return R.fail(ResultCode.PARAM_VALID_ERROR, "accountid不能为空");
        }
        return articleService.getDrafts(accountid, keyword);
    }

    @GetMapping("/waitPublish/{accountid}")
    @ApiOperation("用户待审核文章列表")
    @ApiImplicitParam(name = "accountid", value = "用户账号", paramType = "path")
    public R<List<Article>> waitPublish(@PathVariable String accountid,
                                        @RequestParam(value = "keyword", required = false) String keyword) {
        if (!StringUtils.hasText(accountid)) {
            return R.fail(ResultCode.PARAM_VALID_ERROR, "accountid不能为空");
        }
        return articleService.waitPublish(accountid, keyword);
    }


    @PostMapping("/postUserProduct/{accountid}")
    @ApiOperation(value = "商家发布产品", notes = "需要传递用户是否发布文章字段isPublished 默认是未发布")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pictures", value = "已上传的图片链接集合", paramType = "query"),
            @ApiImplicitParam(name = "tags", value = "标签集合", paramType = "query"),
            @ApiImplicitParam(name = "accountid", value = "用户id", paramType = "path")
    })
    public R postProduct(@RequestParam String[] pictures,
                         @RequestBody Article article,
                         @RequestParam String[] tags, @PathVariable String accountid) {
        if (StringUtils.isEmpty(accountid)) {
            return R.fail(ResultCode.PARAM_MISS, "accountid不能为空");
        }
        return articleService.postProduct(pictures, article, tags, accountid);
    }

    @PostMapping("/postUserArticle/{accountid}")
    @ApiOperation(value = "用户发布文章", notes = "需要传递用户是否发布文章字段isPublished 默认是未发布")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pictures", value = "已上传的图片链接集合", paramType = "query"),
            // @ApiImplicitParam(name = "article", value = "文章实体", paramType = "body"),
            @ApiImplicitParam(name = "tags", value = "标签集合", paramType = "query"),
            @ApiImplicitParam(name = "accountid", value = "用户id", paramType = "path")
    })
    public R postArticle(@RequestParam String[] pictures,
                         @RequestBody Article article,
                         @RequestParam String[] tags, @PathVariable String accountid) {
        if (StringUtils.isEmpty(accountid)) {
            return R.fail(ResultCode.PARAM_MISS, "accountid不能为空");
        }
        return articleService.postArticle(pictures, article, tags, accountid);
    }

    @PutMapping("/updateUserArticle")
    @ApiOperation(value = "用户更新文章")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pictures", value = "已上传的图片链接集合", paramType = "query"),
            @ApiImplicitParam(name = "tags", value = "标签集合", paramType = "query"),
    })
    public R updateArticle(@RequestParam String[] pictures,
                           @RequestBody Article article,
                           @RequestParam String[] tags) {
        return articleService.putArticle(pictures, article, tags);
    }

    @DeleteMapping("/userDeleteArticle/{articleid}")
    @ApiOperation(value = "用户删除文章")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "accountid", value = "用户账号", paramType = "path"),
            @ApiImplicitParam(name = "articleid", value = "文章id", paramType = "path")
    })
    public R deleteArticle(@PathVariable Integer articleid) {
        if (ObjectUtil.hasEmpty(articleid)) {
            R.fail(ResultCode.PARAM_VALID_ERROR, "用户账号或文章id不能为空");
        }
        return articleService.deleteArticle(articleid);
    }

    @GetMapping("/vagueSelect/{accountid}/{label}")
    @ApiOperation(value = "用户我的模块中查询自己已发布或收藏、点赞的文章", notes = "label(0:自己发布的文章，1:点赞，2:收藏)")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "accountid", value = "用户账号id", paramType = "path"),
            @ApiImplicitParam(name = "content", value = "模糊查询关键字", paramType = "query"),
            @ApiImplicitParam(name = "label", value = "0:自己发布的文章，1:点赞，2:收藏", paramType = "path")
    })
    public R<List<Article>> vagueSelect(@PathVariable String accountid,
                                        @RequestParam(required = false) String content, @PathVariable String label) {
        if (ObjectUtil.hasEmpty(accountid, label)) {
            R.fail(ResultCode.PARAM_MISS);
        }
        return articleService.vagueSelect(accountid, content, label);

    }


    /**
     * 收藏用户文章
     *
     * @return
     */
    @PostMapping("/collectionUserArticle")
    @ApiOperation(value = "用户收藏其他用户的文章", notes = "实体内包含accountid：收藏操作发起者，articleid：被收藏的文章")
    public R collectionUserArticle(@RequestBody ArticleCollection articleCollection) {
        if (ObjectUtil.hasEmpty(articleCollection.getAccountid(), articleCollection.getArticleid())) {
            return R.fail(ResultCode.PARAM_VALID_ERROR);
        }
        return articleCollectionService.collectionUserArticle(articleCollection);
    }

  /*@DeleteMapping("/userDeleteArticle/{articleid}")
  @ApiOperation(value = "删除文章")
  @ApiImplicitParam(name = "articleid", value = "文章id", paramType = "path")
  public R deleteArticleById(@PathVariable Long articleid) {
    if (ObjectUtil.hasEmpty(articleid)) {
      R.fail(ResultCode.PARAM_MISS, "文章id不能为空");
    }
    return articleService.deleteArticleById(articleid);
  }*/

}
