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
 * 活动预约表
 *
 * @TableName cs_activity_appointment
 */
@TableName(value = "cs_activity_appointment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ActivityAppointment", description = "")
public class ActivityAppointment implements Serializable {
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
     * 用户账号
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "用户账号")
    private String accountid;

    /**
     * 是否删除 0未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除")
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

    /**
     * 活动信息
     */
    @TableField(exist = false)
    @ApiModelProperty("活动信息")
    private Activity activity;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public ActivityAppointment(Long activityid, String accountid) {
        this.activityid = activityid;
        this.accountid = accountid;
    }
}