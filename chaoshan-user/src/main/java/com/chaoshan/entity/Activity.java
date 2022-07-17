package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 活动表
 *
 * @author YCE
 * @TableName cs_activity
 */
@TableName(value = "cs_activity")
@Data
public class Activity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 活动图链接
     */
    @TableField(value = "picture_link")
    private String pictureLink;

    /**
     * 正文
     */
    @TableField(value = "details")
    private String details;

    /**
     * 是否删除 0未删除 1已删除  default 0
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    /**
     * 活动开始时间
     */
    @TableField(value = "start_time")
    private LocalTime startTime;

    /**
     * 活动结束时间
     */
    @TableField(value = "end_time")
    private LocalTime endTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 国历
     */
    @TableField(value = "calendar")
    private LocalDate calendar;

    /**
     * 农历
     */
    @TableField(value = "lunar")
    private String lunar;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}