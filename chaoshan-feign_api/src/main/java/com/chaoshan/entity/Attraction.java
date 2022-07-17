package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @DATE: 2022/05/12 14:04
 * @Author: 小爽帅到拖网速
 */

/**
 * 名人景点表
 */
@ApiModel(value = "com-chaoshan-entity-Attraction")
@Data
@TableName(value = "chaoshan.cs_attraction")
public class Attraction implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 简介
     */
    @TableField(value = "introduction")
    @ApiModelProperty(value = "简介")
    private String introduction;

    /**
     * 正文
     */
    @TableField(value = "details")
    @ApiModelProperty(value = "正文")
    private String details;

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
     * 视频链接
     */
    @TableField(value = "vedio_link")
    @ApiModelProperty(value = "视频链接")
    private String vedioLink;

    /**
     * 当前位置
     */
    @TableField(value = "location")
    @ApiModelProperty(value = "当前位置")
    private String location;

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