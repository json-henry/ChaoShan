package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @DATE: 2022/05/12 14:04
 * @Author: 小爽帅到拖网速
 */

/**
 * 用户消息表
 */
@Data
@Accessors(chain = true)
public class UserMessage implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

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
     * 文章id
     */
    private Long articleid;
    /**
     * 消息内容
     */
    @TableField(value = "message")
    @ApiModelProperty(value = "消息内容")
    private String message;

    /**
     * 消息类型 0 活动 1 话题 2 评论 3 回复  4点赞 5收藏 6关注
     */
    private Integer messageType;

    /**
     * 是否已读 0未读取 1已读取 default 0
     */
    @TableField(value = "is_read")
    @ApiModelProperty(value = "是否已读 0未读取 1已读取 default 0")
    private Boolean isRead;

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