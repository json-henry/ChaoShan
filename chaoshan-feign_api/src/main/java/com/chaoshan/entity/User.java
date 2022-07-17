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
 * 用户表
 */
@ApiModel(value = "com-chaoshan-entity-User")
@Data
@TableName(value = "chaoshan.cs_user")
public class User implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户账号
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "用户账号")
    private String accountid;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户密码（加密）
     */
    @TableField(value = "password")
    @ApiModelProperty(value = "用户密码（加密）")
    private String password;

    /**
     * 联系电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 性别  0女 1男 default 1
     */
    @TableField(value = "gender")
    @ApiModelProperty(value = "性别  0女 1男 default 1")
    private Boolean gender;

    /**
     * 邀请码
     */
    @TableField(value = "invitation_code")
    @ApiModelProperty(value = "邀请码")
    private String invitationCode;

    /**
     * 头像链接
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value = "头像链接")
    private String avatar;

    /**
     * 简介
     */
    @TableField(value = "introduction")
    @ApiModelProperty(value = "简介")
    private String introduction;

    /**
     * 商家 0不是 1是 default 0
     */
    @TableField(value = "is_business")
    @ApiModelProperty(value = "商家 0不是 1是 default 0")
    private Boolean isBusiness;

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