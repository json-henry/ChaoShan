package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName cs_activity_reply
 */
@TableName(value = "cs_activity_reply")
@Data
@ApiModel(value = "ActivityReply", description = "")
public class ActivityReply implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
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
    @ApiModelProperty(value = "是否删除")
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
    @ApiModelProperty(value = "")
    private static final long serialVersionUID = 1L;
}