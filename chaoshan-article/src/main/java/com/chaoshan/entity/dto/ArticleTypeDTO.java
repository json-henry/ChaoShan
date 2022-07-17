package com.chaoshan.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @DATE: 2022/05/14 14:08
 * @Author: 小爽帅到拖网速
 */
@Data
@ApiModel
public class ArticleTypeDTO implements Serializable {

    @ApiModelProperty(value = "图片列表")
    @TableField(exist = false)
    private List<String> pictureLinks;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 文章标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "文章标题")
    private String title;

    // /**
    //  * 内容简介，没有简介默认截取正文前25个字
    //  */
    // @TableField(value = "introduction")
    // @ApiModelProperty(value = "内容简介，没有简介默认截取正文前25个字")
    // private String introduction;

    /**
     * 图片链接
     */
    @TableField(value = "picture_link")
    @ApiModelProperty(value = "图片链接")
    @JsonIgnore
    private String pictureLink;

    /**
     * 正文
     */
    @TableField(value = "details")
    @ApiModelProperty(value = "正文")
    private String details;


    /*
     */
/**
 * 推荐月份
 *//*

  @TableField(value = "recommend_method")
  @ApiModelProperty(value = "推荐月份")
  private Integer recommendMethod;

  */
/**
 * 当前位置
 *//*

  @TableField(value = "location")
  @ApiModelProperty(value = "当前位置")
  private String location;
*/


    /**
     * 类型 1饮食文化 2名人景点 3民间艺术 4潮玩攻略
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "类型 1饮食文化 2名人景点 3民间艺术 4潮玩攻略")
    private Integer type;


    /**
     * 浏览量
     */
    @TableField(value = "page_view")
    @ApiModelProperty(value = "浏览量")
    private Long pageView;

    private static final long serialVersionUID = 1L;

}
