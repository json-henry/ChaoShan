package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName cs_activity_comment
 */
@TableName(value = "cs_activity_comment")
@Data
@ApiModel(value = "ActivityComment", description = "")
public class ActivityComment implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 话题id
     */
    @TableField(value = "topic_id")
    @ApiModelProperty(value = "话题id")
    private Long topicId;

    /**
     * 用户账号
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "用户账号")
    private String accountid;

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
    private static final long serialVersionUID = 1L;
}