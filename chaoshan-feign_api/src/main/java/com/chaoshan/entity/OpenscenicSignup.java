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
 * 开放景区报名表
 */
@ApiModel(value = "com-chaoshan-entity-OpenscenicSignup")
@Data
@TableName(value = "chaoshan.cs_openscenic_signup")
public class OpenscenicSignup implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 开发景区id
     */
    @TableField(value = "openscenicid")
    @ApiModelProperty(value = "开发景区id")
    private Long openscenicid;

    /**
     * 用户账号
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "用户账号")
    private Long accountid;

    /**
     * 入场时间段
     */
    @TableField(value = "adminssion_time")
    @ApiModelProperty(value = "入场时间段")
    private String adminssionTime;

    /**
     * 当前位置
     */
    @TableField(value = "current_position")
    @ApiModelProperty(value = "当前位置")
    private String currentPosition;

    /**
     * 是否有14天内接触史 0 否 1 是  default 0
     */
    @TableField(value = "is_contact_history")
    @ApiModelProperty(value = "是否有14天内接触史 0 否 1 是  default 0")
    private Boolean isContactHistory;

    /**
     * 是否绿码 0 否 1 是 default 1
     */
    @TableField(value = "is_health_code")
    @ApiModelProperty(value = "是否绿码 0 否 1 是 default 1")
    private Boolean isHealthCode;

    /**
     * 是否健康 0 否 1 是 default 1
     */
    @TableField(value = "is_health")
    @ApiModelProperty(value = "是否健康 0 否 1 是 default 1")
    private Boolean isHealth;

    /**
     * 是否属实 0 否 1 是 default 1
     */
    @TableField(value = "is_true")
    @ApiModelProperty(value = "是否属实 0 否 1 是 default 1")
    private Boolean isTrue;

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