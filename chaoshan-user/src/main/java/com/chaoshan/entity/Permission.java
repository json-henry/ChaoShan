package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 权限表
 */
@ApiModel(value = "com-chaoshan-entity-Permission")
@Data
@TableName(value = "chaoshan.t_permission")
public class Permission implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private String id;

    /**
     * 权限标识符
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "权限标识符")
    private String code;

    /**
     * 描述
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 请求地址
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "请求地址")
    private String url;

    private static final long serialVersionUID = 1L;
}