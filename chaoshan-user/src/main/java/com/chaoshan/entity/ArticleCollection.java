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
 * 文章收藏表
 */
@ApiModel(value = "com-chaoshan-entity-ArticleCollection")
@Data
@Accessors(chain = true)
@TableName(value = "chaoshan.cs_article_collection")
public class ArticleCollection implements Serializable {
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
     * 是否删除 0未删除 1已删除  default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除 0未删除 1已删除  default 0")
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