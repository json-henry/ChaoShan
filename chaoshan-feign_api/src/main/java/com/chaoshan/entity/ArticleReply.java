package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @DATE: 2022/05/12 09:02
 * @Author: 小爽帅到拖网速
 */

@ApiModel(value = "com-chaoshan-entity-ArticleReply")
@Data
@TableName(value = "chaoshan.cs_article_reply")
public class ArticleReply implements Serializable {

    /**
     * 点赞数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "点赞数", hidden = false)
    private Integer star;


    @TableField(exist = false)
    @ApiModelProperty(value = "回复用户", hidden = true)
    private User replyUser;

    @TableField(exist = false)
    @ApiModelProperty(value = "目标用户", hidden = true)
    private User targetUser;

    @TableField(exist = false)
    @ApiModelProperty(value = "父评论id")
    private Long parentCommentId;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    @ApiModelProperty(value = "评论id")
    private Long commentId;

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
     * 是否删除 0 未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除 0 未删除 1已删除 default 0")
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