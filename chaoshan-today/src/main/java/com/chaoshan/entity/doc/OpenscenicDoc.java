package com.chaoshan.entity.doc;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chaoshan.entity.Openscenic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 开放景区表
 *
 * @author YCE
 * @TableName cs_openscenic
 */
@TableName(value = "cs_openscenic")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "OpenscenicDoc", description = "")
public class OpenscenicDoc implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 景区名称
     */
    @ApiModelProperty(value = "景区名称")
    private String name;

    /**
     * 景区图
     */
    @ApiModelProperty(value = "景区图")
    private String pictureLink;

    /**
     * 景区的描述、内容
     */
    @ApiModelProperty(value = "景区的描述、内容")
    private String contents;

    /**
     * 准许进入人数
     */
    @ApiModelProperty(value = "准许进入人数")
    private Integer accessibleNumber;

    /**
     * 是否删除 0未删除 1已删除 default 0
     */
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

    /**
     * 开放时间
     */
    @ApiModelProperty(value = "开放时间")
    private LocalTime openTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    /**
     * 是否开放 0 不开放  1 开放  default 1
     */
    @ApiModelProperty(value = "是否开放")
    private Integer isOpen;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String location;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 距离的位置
     */
    @ApiModelProperty(value = "距离的位置")
    private Object distance;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    private static final long serialVersionUID = 1L;

    public OpenscenicDoc(Openscenic openscenic) {
        this.id = openscenic.getId();
        this.name = openscenic.getName();
        this.pictureLink = openscenic.getPictureLink();
        this.contents = openscenic.getContents();
        this.accessibleNumber = openscenic.getAccessibleNumber();
        this.isDelete = openscenic.getIsDelete();
        this.openTime = openscenic.getOpenTime();
        this.endTime = openscenic.getEndTime();
        this.isOpen = openscenic.getIsOpen();
        this.location = openscenic.getLatitude() + "," + openscenic.getLongitude();
        this.createTime = openscenic.getCreateTime();
        this.createBy = openscenic.getCreateBy();
        this.updateTime = openscenic.getUpdateTime();
        this.updateBy = openscenic.getUpdateBy();
    }
}