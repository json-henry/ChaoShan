package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */

/**
 * 角色表
 */
@ApiModel(value = "com-chaoshan-entity-Role")
@Data
@TableName(value = "chaoshan.t_role")
public class Role implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private String id;

    @TableField(value = "role_name")
    @ApiModelProperty(value = "")
    private String roleName;

    @TableField(value = "description")
    @ApiModelProperty(value = "")
    private String description;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "")
    private Date updateTime;

    @TableField(value = "status")
    @ApiModelProperty(value = "")
    private String status;

    private static final long serialVersionUID = 1L;
}