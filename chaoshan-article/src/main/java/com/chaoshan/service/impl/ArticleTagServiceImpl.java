package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.ArticleTag;
import com.chaoshan.mapper.ArticleTagMapper;
import com.chaoshan.service.ArticleTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @DATE: 2022/05/13 14:39
 * @Author: 小爽帅到拖网速
 */

@Service
@Slf4j
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 根据tag “1,2,3,4” 获取标签名
     *
     * @param tag
     * @return
     */
    @Override
    public Object[] getTagsByIds(String tag) {
        String[] tagIds = tag.split(",");
        Object[] tagNameArray = ((Collection<ArticleTag>) this.listByIds(CollectionUtils.arrayToList(tagIds))).stream()
                .map(ArticleTag::getTagName).toArray();
        return tagNameArray;
    }

    /**
     * 根据标签字符串数组返回对应标签表里的id并拼接长字符串
     *
     * @param tags
     * @return
     */
    @Override
    public String getIdsByTags(String[] tags) {
        // 把标签写入数据库
        List<ArticleTag> articleTags = Arrays.stream(tags).map(tag -> {
            if (articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagName, tag)) > 0) {
                return null;
            } else {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagName(tag);
                if (articleTagMapper.insert(articleTag) == 0) {
                    log.error("标签：{}插入失败", tag);
                    return null;
                }
                return articleTag;
            }
        }).collect(Collectors.toList());

        List<Long> idsList = this.list(new LambdaQueryWrapper<ArticleTag>().select(ArticleTag::getId)
                        .in(tags.length > 0, ArticleTag::getTagName, Arrays.asList(tags)))
                .stream().map(articleTag -> articleTag.getId()).collect(Collectors.toList());
        String idsStr = idsList.stream().map(tag -> tag.toString()).collect(Collectors.joining(","));
        return idsStr;
    }
}
