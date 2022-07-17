package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 呱呱
 * @date Created in 2022/5/19 20:48
 */

/**
 * 用户邀请表
 */
@ApiModel(value = "com-chaoshan-entity-UserInvitation")
@Data
@Accessors(chain = true)
@TableName(value = "chaoshan.cs_user_invitation")
public class UserInvitation implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 邀请方账号
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "邀请方账号")
    private String accountid;

    /**
     * 被邀请方账号
     */
    @TableField(value = "accountided")
    @ApiModelProperty(value = "被邀请方账号")
    private String accountided;

    /**
     * 是否删除 0 未删除  1 已删除  default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除 0 未删除  1 已删除  default 0")
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