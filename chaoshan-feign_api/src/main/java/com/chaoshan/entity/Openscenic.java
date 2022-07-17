package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
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
public class Openscenic implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 景区名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 景区图
     */
    @TableField(value = "picture_link")
    private String pictureLink;

    /**
     * 景区的描述、内容
     */
    @TableField(value = "contents")
    private String contents;

    /**
     * 准许进入人数
     */
    @TableField(value = "accessible_number")
    private Integer accessibleNumber;

    /**
     * 是否删除 0未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    /**
     * 开放时间
     */
    @TableField(value = "open_time")
    private LocalTime openTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private LocalTime endTime;

    /**
     * 是否开放 0 不开放  1 开放  default 1
     */
    @TableField(value = "is_open")
    private Integer isOpen;

    /**
     * 经度
     */
    @TableField(value = "longitude")
    private String longitude;

    /**
     * 纬度
     */
    @TableField(value = "latitude")
    private String latitude;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}