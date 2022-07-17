package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */

/**
 * 角色权限表
 */
@ApiModel(value = "com-chaoshan-entity-RolePermission")
@Data
@TableName(value = "chaoshan.t_role_permission")
public class RolePermission implements Serializable {
    @TableId(value = "role_id", type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private String roleId;

    @TableId(value = "permission_id", type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private String permissionId;

    private static final long serialVersionUID = 1L;
}