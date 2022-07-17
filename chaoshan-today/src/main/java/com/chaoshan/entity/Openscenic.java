package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 开放景区表
 *
 * @TableName cs_openscenic
 */
@TableName(value = "cs_openscenic")
@Data
@Accessors(chain = true)
@ApiModel(value = "Openscenic", description = "")
public class Openscenic implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 景区名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "景区名称")
    private String name;

    /**
     * 景区图
     */
    @TableField(value = "picture_link")
    @ApiModelProperty(value = "景区图")
    private String pictureLink;

    /**
     * 景区的描述、内容
     */
    @TableField(value = "contents")
    @ApiModelProperty(value = "景区的描述、内容")
    private String contents;

    /**
     * 准许进入人数
     */
    @TableField(value = "accessible_number")
    @ApiModelProperty(value = "准许进入人数")
    private Integer accessibleNumber;

    /**
     * 是否删除 0未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Integer isDelete;

    /**
     * 开放时间
     */
    @TableField(value = "open_time")
    @ApiModelProperty(value = "开放时间")
    private LocalTime openTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    /**
     * 是否开放 0 不开放  1 开放  default 1
     */
    @TableField(value = "is_open")
    @ApiModelProperty(value = "是否开放")
    private Integer isOpen;

    /**
     * 经度
     */
    @TableField(value = "longitude")
    @ApiModelProperty(value = "经度")
    private String longitude;

    /**
     * 纬度
     */
    @TableField(value = "latitude")
    @ApiModelProperty(value = "纬度")
    private String latitude;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}