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
 * 商家认证表
 */
@ApiModel(value = "com-chaoshan-entity-Seller")
@Data
@TableName(value = "chaoshan.cs_seller")
@Accessors(chain = true)
public class Seller implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户账号
     */
    @TableField(value = "accountid")
    @ApiModelProperty(value = "用户账号")
    private String accountid;

    /**
     * 图片凭证
     */
    @TableField(value = "picture_credential")
    @ApiModelProperty(value = "图片凭证")
    private String pictureCredential;

    /**
     * 是否审核 0 未审核 1已审核 default 0
     */
    @TableField(value = "is_check")
    @ApiModelProperty(value = "是否审核 0 未审核 1已审核 default 0")
    private Boolean isCheck;

    /**
     * 是否删除 0 已删除 1 未删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除 0 已删除 1 未删除 default 0")
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