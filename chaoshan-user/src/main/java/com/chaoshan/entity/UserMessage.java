package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 呱呱
 * @date Created in 2022/5/19 20:48
 */

/**
 * 用户消息表
 */
@ApiModel(value = "com-chaoshan-entity-UserMessage")
@Data
@Accessors(chain = true)
@TableName(value = "chaoshan.cs_user_message")
public class UserMessage implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 文章id
     */
    @TableField(value = "articleid")
    @ApiModelProperty(value = "文章id")
    private Long articleid;

    /**
     * 发送方账号
     */
    @TableField(value = "send_accountid")
    @ApiModelProperty(value = "发送方账号")
    private String sendAccountid;

    /**
     * 接收方账号
     */
    @TableField(value = "receive_accountid")
    @ApiModelProperty(value = "接收方账号")
    private String receiveAccountid;

    /**
     * 消息内容
     */
    @TableField(value = "message")
    @ApiModelProperty(value = "消息内容")
    private String message;

    /**
     * 是否已读 0未读取 1已读取 default 0
     */
    @TableField(value = "is_read")
    @ApiModelProperty(value = "是否已读 0未读取 1已读取 default 0")
    private Boolean isRead;

    /**
     * 消息类型    0 活动 1 话题 2 评论 3 回复  4点赞 5收藏 6关注
     */
    @TableField(value = "message_type")
    @ApiModelProperty(value = "消息类型    0 活动 1 话题 2 评论 3 回复  4点赞 5收藏 6关注")
    private Integer messageType;

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

    /**
     * 逻辑删除 0 未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "逻辑删除 0 未删除 1已删除 default 0")
    @TableLogic
    private Boolean isDelete;

    private static final long serialVersionUID = 1L;
}