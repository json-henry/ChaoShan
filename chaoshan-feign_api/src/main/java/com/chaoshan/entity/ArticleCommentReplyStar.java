package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @DATE: 2022/05/14 08:33
 * @Author: 小爽帅到拖网速
 */

/**
 * 文章评论回复点赞表
 */
@ApiModel(value = "com-chaoshan-entity-ArticleCommentReplyStar")
@Data
@TableName(value = "chaoshan.cs_article_comment_reply_star")
public class ArticleCommentReplyStar implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
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
    @TableField(value = "comment_reply_id")
    @ApiModelProperty(value = "评论id")
    private Long commentReplyId;

    /**
     * 删除 0 未删除 1 已删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "删除 0 未删除 1 已删除 default 0")
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