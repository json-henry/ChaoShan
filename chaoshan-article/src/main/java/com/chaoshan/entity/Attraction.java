package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@ApiModel(value = "com-chaoshan-entity-Attraction")
@Data
@TableName(value = "chaoshan.cs_attraction")
public class Attraction implements Serializable {
    /**
     * 文章id
     */
    @TableField(value = "articleid")
    @ApiModelProperty(value = "文章id")
    private Long articleid;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 场景 室内或室外
     */
    @TableField(value = "scene")
    @ApiModelProperty(value = "场景 室内或室外")
    private Object scene;

    /**
     * 评分
     */
    @TableField(value = "score")
    @ApiModelProperty(value = "评分")
    private Short score;

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
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}