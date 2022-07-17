package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户消息表
 *
 * @TableName cs_user_message
 */
@TableName(value = "cs_user_message")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    @TableField(value = "articleid")
    private Long articleid;

    /**
     * 发送方账号
     */
    @TableField(value = "send_accountid")
    private String sendAccountid;

    /**
     * 接收方账号
     */
    @TableField(value = "receive_accountid")
    private String receiveAccountid;

    /**
     * 消息内容
     */
    @TableField(value = "message")
    private String message;

    /**
     * 是否已读 0未读取 1已读取 default 0
     */
    @TableField(value = "is_read")
    private Integer isRead;

    /**
     * 消息类型    0 活动 1 话题 2 评论 3 回复  4点赞 5收藏 6关注
     */
    @TableField(value = "message_type")
    private Integer messageType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除 0 未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

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