package com.chaoshan.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.Article;
import com.chaoshan.entity.User;
import com.chaoshan.entity.UserArticle;
import com.chaoshan.entity.dto.ArticleTypeDTO;
import com.chaoshan.mapper.UserArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @DATE: 2022/05/13 18:18
 * @Author: 小爽帅到拖网速
 */
@Slf4j
public class UtilMethod {

    public static String getAccountidByArticleId(Long articleid, UserArticleMapper userArticleMapper) {
        UserArticle userArticle = userArticleMapper.selectOne(new LambdaQueryWrapper<UserArticle>()
                .select(UserArticle::getAccountid).eq(UserArticle::getArticleid, articleid));
        if (ObjectUtil.isNotEmpty(userArticle)) {
            return userArticle.getAccountid();
        }
        return null;
    }


    /**
     * 复制用户忽略特定字段，保留用户账号，头像
     *
     * @param userDb
     * @return
     */
    public static User copyUserIgnoreField(User userDb) {
        if (ObjectUtil.isEmpty(userDb)) {
            log.error("userDb值为空");
            return null;
        }
        CopyOptions copyOptions = CopyOptions.create().setIgnoreProperties("id", "username", "password", "phone",
                "gender", "invitationCode", "introduction", "isBusiness", "isDelete", "updateTime", "createTime");
        User commentUser = new User();
        BeanUtil.copyProperties(userDb, commentUser, copyOptions);
        return commentUser;
    }

    /**
     * 复制文章忽略特定字段，保留文章图片链接，标题，简介，id，
     *
     * @param articleDb
     * @return
     */
    public static ArticleTypeDTO copyArticleIgnoreField(Article articleDb) {
        if (ObjectUtil.isEmpty(articleDb)) {
            log.error("articleDb值为空");
            return null;
        }
        ArticleTypeDTO articleTypeDTO = new ArticleTypeDTO();
        BeanUtil.copyProperties(articleDb, articleTypeDTO);
        return articleTypeDTO;
    }

    /**
     * 复制文章忽略特定字段，保留文章图片链接，标题，简介，id，
     *
     * @param articleDb
     * @return
     */
    public static Article copyArticleIgnoreFieldNoDTO(Article articleDb) {
        if (ObjectUtil.isEmpty(articleDb)) {
            log.error("article值为空");
            return null;
        }
        Article handleArticle = new Article();
        BeanUtil.copyProperties(articleDb, handleArticle);
        return handleArticle;
    }

    /**
     * 根据参数symbol分割字符串成字符串数组
     *
     * @param linkString
     * @param symbol
     * @return
     */
    public static List<String> divisionLinkString(String linkString, String symbol) {
        if (ObjectUtil.hasEmpty(linkString, symbol)) {
            log.error("linkString或者symbol值为空");
            return null;
        }
        List<String> stringList = Arrays.asList(linkString.split(symbol)).stream()
                .map(object -> object.toString()).collect(Collectors.toList());
        return stringList;
    }

}
