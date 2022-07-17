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
 * 关注表
 */
@ApiModel(value = "com-chaoshan-entity-Fans")
@Data
@TableName(value = "chaoshan.cs_fans")
public class Fans implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 关注用户账号
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "关注用户账号")
    private String accountid;

    /**
     * 粉丝用户账号
     */
    @TableField(value = "accountided")
    @ApiModelProperty(value = "粉丝用户账号")
    private String accountided;

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