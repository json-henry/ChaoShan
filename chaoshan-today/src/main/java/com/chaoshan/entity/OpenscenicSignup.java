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
 * 开放景区报名表
 *
 * @TableName cs_openscenic_signup
 */
@TableName(value = "cs_openscenic_signup")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "OpenscenicSignup", description = "")
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
    private String accountid;

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
    @ApiModelProperty(value = "是否有14天内接触史")
    private Integer isContactHistory;

    /**
     * 是否绿码 0 否 1 是 default 1
     */
    @TableField(value = "is_health_code")
    @ApiModelProperty(value = "是否绿码")
    private Integer isHealthCode;

    /**
     * 是否健康 0 否 1 是 default 1
     */
    @TableField(value = "is_health")
    @ApiModelProperty(value = "是否健康")
    private Integer isHealth;

    /**
     * 是否属实 0 否 1 是 default 1
     */
    @TableField(value = "is_true")
    @ApiModelProperty(value = "是否属实")
    private Integer isTrue;

    /**
     * 是否删除 0 未删除 1已删除 default 0
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
     * 景区信息
     */
    @TableField(exist = false)
    @ApiModelProperty("景区信息")
    public Openscenic openscenic;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public OpenscenicSignup(Long openscenicid, String accountid) {
        this.openscenicid = openscenicid;
        this.accountid = accountid;
    }
}