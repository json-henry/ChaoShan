package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户消息表
 *
 * @author Hyx
 * @since 2022-05-13
 */

@Data
@TableName(value = "chaoshan.cs_user_message")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "UserMessage", description = "消息模型")
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
    @ApiModelProperty(value = "")
    private Long articleid;


    /**
     * 发送方账号
     */
    @TableField(value = "send_accountid")
    @ApiModelProperty(value = "")
    private String sendAccountid;

    /**
     * 接收方账号
     */
    @TableField(value = "receive_accountid")
    @ApiModelProperty(value = "接收方账号")
    private String receiveAccountid;


    /**
     * 消息类型 0 活动 1 话题 2 评论 3 回复  4点赞 default 0
     */
    @TableField(value = "message_type")
    @ApiModelProperty(value = "消息类型 0 活动 1 话题 2 评论 3 回复  4点赞 default 0")
    private Integer messageType;

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


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public UserMessage(Long articleid, String sendAccountid, String receiveAccountid, String message,
                       Integer messageType) {
        this.articleid = articleid;
        this.sendAccountid = sendAccountid;
        this.receiveAccountid = receiveAccountid;
        this.message = message;
        this.messageType = messageType;
    }
}