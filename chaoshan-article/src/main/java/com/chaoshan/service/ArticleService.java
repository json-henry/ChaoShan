package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.Article;
import com.chaoshan.entity.dto.ArticleSearchDTO;
import com.chaoshan.entity.dto.ArticleTypeListDTO;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.Rpage;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

public interface ArticleService extends IService<Article> {

    /**
     * 分页获取文章数据
     *
     * @param currentPage 当前页
     * @param size        记录数
     * @param type        类型
     * @return
     */
    R<Rpage> getArtPage(Integer currentPage, Integer size, Integer type);

    /**
     * 每日推荐
     *
     * @return
     */
    R<List<ArticleSearchDTO>> recommendationDaily();

    /**
     * 根据文章id返回文章详情
     *
     * @param id
     * @return
     */
    R<Article> getArticleById(Integer id, HttpServletRequest request);

    /**
     * 根据类型返回文章列表,类型：1(饮食文化) 2(名人景点) 3(民间艺术) 4(潮玩攻略)
     *
     * @param type
     * @param count
     * @return
     */
    R<ArticleTypeListDTO> getArticleListByType(Integer type, Integer count);

    /**
     * 用户查询已发布的文章,包括封面，标题，点赞数，收藏数，时间
     *
     * @param accountid
     * @return
     */
    R<List<Article>> userPubilshedArticle(String accountid, String keyword);

    /**
     * 用户查询草稿箱文章,包括封面，标题，点赞数，收藏数，时间
     *
     * @param accountid
     * @return
     */
    R<List<Article>> getDrafts(String accountid, String content);

    /**
     * 用户收藏文章列表
     *
     * @param accountid
     * @return
     */
    R<List<Article>> getUserCollectionArticles(String accountid, String keyword);

    /**
     * 用户点赞文章列表
     *
     * @param accountid
     * @return
     */
    R<List<Article>> userStarArticles(String accountid, String keyword);

    /**
     * 商家发布产品
     *
     * @param pictures
     * @param article
     * @param tags
     * @return
     */
    R postProduct(String[] pictures, Article article, String[] tags, String accountid);

    /**
     * 用户发布产品
     *
     * @param pictures
     * @param article
     * @param tags
     * @return
     */
    R postArticle(String[] pictures, Article article, String[] tags, String accountid);

    /**
     * 待审核文章列表
     *
     * @param accountid
     * @return
     */
    R<List<Article>> waitPublish(String accountid, String keyword);

    /**
     * 根据文章id修改文章审核状态
     *
     * @param accountids
     * @return
     */
    R examineArticle(String[] accountids);

    /**
     * 用户删除文章
     *
     * @param articleid
     * @return
     */
    R deleteArticle(Integer articleid);

    /**
     * 用户我的模块中查询自己已发布或收藏、点赞的文章
     *
     * @param accountid
     * @param content
     * @param label
     * @return
     */
    R<List<Article>> vagueSelect(String accountid, String content, String label);

    /**
     * 根据文章id更新文章浏览量
     *
     * @param articleid
     * @param size
     */
    boolean updateArticlePageview(String articleid, Long size);

    /**
     * 根据文章id删除文章
     *
     * @param accountid
     * @return
     */
    R deleteArticleById(Long accountid);


    /**
     * 用户更新文章
     *
     * @param pictures
     * @param article
     * @param tags
     * @return
     */
    R putArticle(String[] pictures, Article article, String[] tags);

    /**
     * 删除相关数据，评论，回复，评论点赞，回复点赞，收藏
     *
     * @param articleid
     * @return
     */
    void deleteRelatedData(String articleid);

    /**
     * 根据文章id判断当前登录用户是否点赞收藏
     *
     * @param articleid
     * @return
     */
    R<Map<String, String>> getLikeCollectStatus(String articleid);

    /**
     * 获取轮播图
     *
     * @return
     */
    R<List<ArticleSearchDTO>> shufflingfigure() throws IOException;
}
