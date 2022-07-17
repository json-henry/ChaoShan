package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @DATE: 2022/05/13 14:39
 * @Author: 小爽帅到拖网速
 */

/**
 * 文章评论点赞表
 */
@ApiModel(value = "com-chaoshan-entity-ArticleCommentStar")
@Data
@TableName(value = "chaoshan.cs_article_comment_star")
public class ArticleCommentStar implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 评论点赞操作发起者用户id
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "评论操作发起者用户id")
    private String accountid;

    /**
     * 文章表id
     */
    @TableField(value = "articleid")
    @ApiModelProperty(value = "文章id")
    private Long articleid;

    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 是否删除 0 否 1 是  default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除 0 否 1 是  default 0")
    @TableLogic
    private Boolean isDelete;

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

    private static final long serialVersionUID = 1L;
}