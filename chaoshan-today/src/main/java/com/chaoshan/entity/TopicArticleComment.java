package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 话题文章评论表
 *
 * @TableName cs_topic_article_comment
 */
@TableName(value = "cs_topic_article_comment")
@Data
@ApiModel(value = "TopicArticleComment", description = "")
public class TopicArticleComment implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 发送方账号
     */
    @TableField(value = "send_accoutid")
    @ApiModelProperty(value = "发送方账号")
    private String sendAccoutid;

    /**
     * 话题文章id
     */
    @TableField(value = "article_id")
    @ApiModelProperty(value = "话题文章id")
    private Long articleId;

    /**
     * 消息内容
     */
    @TableField(value = "message")
    @ApiModelProperty(value = "消息内容")
    private String message;


    /**
     * 删除 0 未删除 1 已删除 default 0
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
}