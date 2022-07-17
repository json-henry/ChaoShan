package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 话题文章评论点赞表
 *
 * @author YCE
 * @TableName cs_topic_article_comment_star
 */
@TableName(value = "cs_topic_article_comment_star")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TopicArticleCommentStar", description = "")
public class TopicArticleCommentStar implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "用户id")
    private String accountid;

    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    @ApiModelProperty(value = "评论id")
    private Long commentId;


    /**
     * 是否点赞 0否 1是  默认是1
     */
    @TableField(value = "is_star")
    @ApiModelProperty(value = "是否点赞")
    private Integer isStar;


    /**
     * 删除  0 未删除  1已删除 default 0
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

    public TopicArticleCommentStar(String accountid, Long commentId) {
        this.accountid = accountid;
        this.commentId = commentId;
    }
}