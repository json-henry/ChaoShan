package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@ApiModel(value = "com-chaoshan-entity-ArticleComment")
@Data
@TableName(value = "chaoshan.cs_article_comment")
public class ArticleComment implements Serializable {

    @ApiModelProperty(value = "子回复评论", hidden = true)
    private List<ArticleReply> children;

    @ApiModelProperty(value = "发送方账号信息", hidden = true)
    private User sendUser;

    @TableField(exist = false)
    @ApiModelProperty(value = "当前登录用户是否对该评论点赞")
    private boolean isLike;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 发送方账号
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "发送方账号")
    private String accountid;

    /**
     * 文章id
     */
    @TableField(value = "articleid")
    @ApiModelProperty(value = "文章id")
    private Long articleid;

    /**
     * 消息内容
     */
    @TableField(value = "message")
    @ApiModelProperty(value = "消息内容")
    private String message;

    /**
     * 点赞数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "点赞数", hidden = false)
    private Integer star;

    /**
     * 是否删除 0未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除 0未删除 1已删除 default 0")
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