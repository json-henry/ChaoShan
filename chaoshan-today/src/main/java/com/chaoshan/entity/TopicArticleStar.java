package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 话题文章点赞表
 *
 * @TableName cs_topic_article_star
 */
@TableName(value = "cs_topic_article_star")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TopicArticleStar", description = "")
public class TopicArticleStar implements Serializable {
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
     * 话题文章id
     */
    @TableField(value = "article_id")
    @ApiModelProperty(value = "话题文章id")
    private Long articleId;

    /**
     * 是否点赞 0 否 1点赞  默认是1
     */
    @TableField(value = "is_star")
    @ApiModelProperty(value = "是否点赞")
    private Integer isStar;

    /**
     * 删除 0 未删除 1已删除 default 0
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

    public TopicArticleStar(String sendAccountid, Long articleId) {
        this.sendAccountid = sendAccountid;
        this.articleId = articleId;
    }
}