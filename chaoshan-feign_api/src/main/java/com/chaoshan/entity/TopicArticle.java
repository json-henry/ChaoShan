package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动文章表
 *
 * @author YCE
 * @TableName cs_topic_article
 */
@TableName(value = "cs_topic_article")
@Data
public class TopicArticle implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 话题id
     */
    @TableField(value = "topic_id")
    private Long topicId;

    /**
     * 用户账号
     */
    @TableField(value = "accountid")
    private String accountid;


    /**
     * 文章内容
     */
    @TableField(value = "content")
    private String content;


    /**
     * 图片链接
     */
    @TableField(value = "picture_link")
    private String pictureLink;

    /**
     * 是否删除 0 未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}