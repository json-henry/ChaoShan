package com.chaoshan.clients;

import com.chaoshan.clients.fallback.ArticleClientFallbackFactory;
import com.chaoshan.entity.Article;
import com.chaoshan.entity.ArticleCollection;
import com.chaoshan.entity.ArticleStar;
import com.chaoshan.util.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 呱呱
 * @date Created in 2022/5/13 13:38
 */
@FeignClient(value = "CHAOSHAN-ARTICLE", fallbackFactory = ArticleClientFallbackFactory.class)
public interface ArticleClient {

    String prefix_ = "/article/api";

    /**
     * @param articleid
     * @return 文章详情内容
     */
    @GetMapping(prefix_ + "/getArticleDetail/{articleid}")
    R<Article> getArticleDetail(@PathVariable("articleid") Integer articleid);

    /**
     * @param accountid
     * @return 用户自作文章列表
     */
    @GetMapping(prefix_ + "/userPubilshedArticle/{accountid}")
    R<List<Article>> getArticleList(@PathVariable("accountid") String accountid);


    /**
     * @param accountid
     * @return 用户草稿箱文章列表
     */
    @GetMapping(prefix_ + "/draftsArticles/{accountid}")
    R<List<Article>> getDrafts(@PathVariable("accountid") String accountid, @RequestParam(value = "keyword",
            required = false) String keyword);


    /**
     * @param accountid
     * @return 待审核文章列表
     */
    @GetMapping(prefix_ + "/waitPublish/{accountid}")
    R<List<Article>> waitPublish(@PathVariable("accountid") String accountid, @RequestParam(value = "keyword",
            required = false) String keyword);

    /**
     * @param pictures
     * @param article
     * @param tags
     * @return 发布文章
     */
    @PostMapping(prefix_ + "/postUserArticle/{accountid}")
    R postArticle(@RequestParam("pictures") String[] pictures, @RequestBody Article article,
                  @RequestParam("tags") String[] tags, @PathVariable("accountid") String accountid);

    /**
     * @param pictures
     * @param article
     * @param tags
     * @return 发布产品
     */
    @PostMapping(prefix_ + "/postUserProduct/{accountid}")
    R postProduct(@RequestParam("pictures") String[] pictures, @RequestBody Article article,
                  @RequestParam("tags") String[] tags, @PathVariable("accountid") String accountid);

    /**
     * @param accountid
     * @param content
     * @return 模糊查询文章
     * @Param label  值为"文章/点赞/收藏“
     */
    @GetMapping(prefix_ + "/vagueSelect/{accountid}/{label}")
    R<List<Article>> vagueSelect(@PathVariable("accountid") String accountid, @RequestParam(required = false, value =
            "content") String content, @PathVariable("label") String label);

    /**
     * 点赞用户文章
     *
     * @return
     */
    @PostMapping(prefix_ + "/starUserArticle")
    R starArticleOper(@RequestBody ArticleStar articleStar);


    /**
     * 收藏用户文章
     *
     * @return
     */
    @GetMapping(prefix_ + "/collectionUserArticle")
    R collectionUserArticle(@RequestBody ArticleCollection articleCollection);

    /**
     * 用户更新文章
     *
     * @param pictures
     * @param article
     * @param tags
     * @return
     */
    @PutMapping(prefix_ + "/updateUserArticle")
    R updateArticle(@RequestParam("pictures") String[] pictures,
                    @RequestBody Article article,
                    @RequestParam("tags") String[] tags);
}
