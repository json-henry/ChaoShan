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
 * 话题表
 *
 * @TableName cs_topic
 */
@TableName(value = "cs_topic")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "Topic", description = "")
public class Topic implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 活动id
     */
    @TableField(value = "activityid")
    @ApiModelProperty(value = "活动id")
    private Long activityid;

    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 活动简介
     */
    @TableField(value = "introduction")
    @ApiModelProperty(value = "活动简介")
    private String introduction;

    /**
     * 浏览量
     */
    @TableField(value = "view_count")
    @ApiModelProperty(value = "浏览量")
    private Long viewCount;

    /**
     * 是否删除 0未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Integer isDelete;

    /**
     * 发布时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否有热标签")
    private Boolean isHot;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否有新标签")
    private Boolean isNew;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}