package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 活动表
 *
 * @author YCE
 * @TableName cs_activity
 */
@TableName(value = "cs_activity")
@Data
@ApiModel(value = "Activity", description = "")
public class Activity implements Serializable {
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
     * 活动图链接
     */
    @TableField(value = "picture_link")
    @ApiModelProperty(value = "活动图链接")
    private String pictureLink;

    /**
     * 正文
     */
    @TableField(value = "details")
    @ApiModelProperty(value = "正文")
    private String details;

    /**
     * 是否删除 0未删除 1已删除  default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Integer isDelete;

    /**
     * 活动开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "活动开始时间")
    private LocalTime startTime;

    /**
     * 活动结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "活动结束时间")
    private LocalTime endTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
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

    /**
     * 国历
     */
    @TableField(value = "calendar")
    @ApiModelProperty(value = "国历")
    private LocalDate calendar;

    /**
     * 农历
     */
    @TableField(value = "lunar")
    @ApiModelProperty(value = "农历")
    private String lunar;

    @TableField(exist = false)
    @ApiModelProperty(value = "相关活动视频")
    private List<ActivityVideo> videos;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}