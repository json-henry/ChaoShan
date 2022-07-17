package com.chaoshan.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.chaoshan.entity.TopicArticle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于返回给前端显示，比原先的实体多加了点赞和评论数和是否是我点赞
 */
@Data
@ApiModel("TopicArticleVO")
public class TopicArticleVO implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 话题id
     */
    @ApiModelProperty("话题id")
    private Long topicId;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String accountid;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 图片链接
     */
    @ApiModelProperty("图片链接")
    private String pictureLink;

    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Long starNum;

    /**
     * 评论数
     */
    @ApiModelProperty("评论数")
    private Long commentNum;

    /**
     * 是否是我点赞
     */
    @ApiModelProperty("是否是我点赞")
    private Boolean isMeStar;

    /**
     * 是否已经关注
     */
    @ApiModelProperty(value = "是否已经关注")
    private Boolean isFocus;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public TopicArticleVO(TopicArticle topicArticle, long starNum, long commentNum, boolean isMeStar,
                          boolean isFocus, String avatar, String username) {
        this.id = topicArticle.getId();
        this.topicId = topicArticle.getTopicId();
        this.accountid = topicArticle.getAccountid();
        this.pictureLink = topicArticle.getPictureLink();
        this.content = topicArticle.getContent();
        this.starNum = starNum;
        this.commentNum = commentNum;
        this.isMeStar = isMeStar;
        this.isFocus = isFocus;
        this.avatar = avatar;
        this.username = username;
    }
}