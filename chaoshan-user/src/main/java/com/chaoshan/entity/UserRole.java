package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */

/**
 * 用户角色表
 */
@ApiModel(value = "com-chaoshan-entity-UserRole")
@Data
@TableName(value = "chaoshan.t_user_role")
public class UserRole implements Serializable {
    @TableId(value = "user_id", type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private Long userId;

    @TableId(value = "role_id", type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private String roleId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "")
    private LocalDateTime createTime;

    @TableField(value = "creator", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "")
    private String creator;

    private static final long serialVersionUID = 1L;
}