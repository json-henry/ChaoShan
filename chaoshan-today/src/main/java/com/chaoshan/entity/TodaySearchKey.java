package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName cs_today_search_key
 */
@TableName(value = "cs_today_search_key")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "TodaySearchKey", description = "")
public class TodaySearchKey implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 搜索关键字
     */
    @TableField(value = "key_word")
    @ApiModelProperty(value = "搜索关键字")
    private String keyWord;

    /**
     * 搜索的次数
     */
    @TableField(value = "count")
    @ApiModelProperty(value = "搜索的次数")
    private Long count;

    /**
     * 是否删除 0未删除 1 删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Integer isDelete;

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