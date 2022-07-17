package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel(value = "com-chaoshan-entity-User")
@Data
@Accessors(chain = true)
@TableName(value = "cs_user")
public class User implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String accountid;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户密码（加密）
     */
    @ApiModelProperty(value = "用户密码（加密）")
    private String password;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 性别  0女 1男 default 1
     */
    @ApiModelProperty(value = "性别  0女 1男 default 1")
    private Boolean gender;

    /**
     * 头像链接
     */
    @ApiModelProperty(value = "头像链接")
    private String avatar;

    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    private String introduction;

    /**
     * 商家 0不是 1是 default 0
     */
    @ApiModelProperty(value = "商家 0不是 1是 default 0")
    private Boolean isBusiness;

    /**
     * 逻辑删除 0未删除 1已删除 default 0
     */
    @ApiModelProperty(value = "逻辑删除 0未删除 1已删除 default 0")
    @TableLogic
    private Boolean isDelete;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}

