package com.chaoshan.vo;

import com.chaoshan.entity.TopicArticleReply;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 话题下的文章评论的回复表
 *
 * @author YCE
 * @TableName cs_topic_article_reply
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("TopicArticleReplyVO")
public class TopicArticleReplyVO implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 话题文章评论id
     */
    @ApiModelProperty("")
    private Long articleCommentId;

    /**
     * 回复用户id
     */
    @ApiModelProperty("")
    private String accountid;

    /**
     * 目标用户id
     */
    @ApiModelProperty("")
    private String toAccountid;

    /**
     * 评论内容
     */
    @ApiModelProperty("")
    private String content;


    /**
     * 创建时间
     */
    @ApiModelProperty("")
    private LocalDateTime createTime;

    /**
     * 点赞数
     */
    @ApiModelProperty("")
    private Integer starNum;

    /**
     * 是否是我点赞
     */
    @ApiModelProperty("是否是我点赞")
    private boolean isMeStar;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    private static final long serialVersionUID = 1L;

    public TopicArticleReplyVO(TopicArticleReply reply, Integer starNum, String avatar, String username,
                               boolean isMeStar) {
        this.id = reply.getId();
        this.articleCommentId = reply.getArticleCommentId();
        this.accountid = reply.getAccountid();
        this.toAccountid = reply.getToAccountid();
        this.content = reply.getContent();
        this.createTime = reply.getCreateTime();
        this.starNum = starNum;
        this.avatar = avatar;
        this.username = username;
        this.isMeStar = isMeStar;
    }
}