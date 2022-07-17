package com.chaoshan.vo;

import com.chaoshan.entity.TopicArticleComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 话题文章评论表
 *
 * @TableName cs_topic_article_comment
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("TopicArticleCommentVO")
public class TopicArticleCommentVO implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 发送方账号
     */
    @ApiModelProperty("发送方账号")
    private String sendAccoutid;

    /**
     * 话题文章id
     */
    @ApiModelProperty("话题文章id")
    private Long articleId;

    /**
     * 消息内容
     */
    @ApiModelProperty("消息内容")
    private String message;

    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer starNum;

    /**
     * 回复数
     */
    @ApiModelProperty("回复数")
    private Integer replyNum;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 是否点赞
     */
    @ApiModelProperty("是否点赞")
    private boolean isMeStar;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 评论下的所有回复
     */
    @ApiModelProperty("评论下的所有回复")
    private List<TopicArticleReplyVO> replies;

    private static final long serialVersionUID = 1L;

    public TopicArticleCommentVO(TopicArticleComment comment, Integer starNum, Integer replyNum, String avatar,
                                 String username, boolean isStar, List<TopicArticleReplyVO> replyVOS) {
        this.id = comment.getId();
        this.sendAccoutid = comment.getSendAccoutid();
        this.articleId = comment.getArticleId();
        this.message = comment.getMessage();
        this.createTime = comment.getCreateTime();
        this.starNum = starNum;
        this.replyNum = replyNum;
        this.avatar = avatar;
        this.username = username;
        this.isMeStar = isStar;
        this.replies = replyVOS;
    }
}