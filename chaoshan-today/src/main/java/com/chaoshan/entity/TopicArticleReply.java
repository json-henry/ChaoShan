package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 话题下的文章评论的回复表
 *
 * @TableName cs_topic_article_reply
 */
@TableName(value = "cs_topic_article_reply")
@Data
@ApiModel(value = "TopicArticleReply", description = "")
public class TopicArticleReply implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 话题评论id  (朋友圈评论的回复)
     */
    @TableField(value = "article_comment_id")
    @ApiModelProperty(value = "话题评论id")
    private Long articleCommentId;

    /**
     * 回复用户id
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "回复用户id")
    private String accountid;

    /**
     * 目标用户id
     */
    @TableField(value = "to_accountid")
    @ApiModelProperty(value = "目标用户id")
    private String toAccountid;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "评论内容")
    private String content;

    /**
     * 删除 0 未删除 1 已删除  default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "删除")
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}