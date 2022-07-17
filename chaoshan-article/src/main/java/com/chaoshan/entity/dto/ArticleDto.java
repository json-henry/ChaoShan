package com.chaoshan.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @DATE: 2022/05/11 21:15
 * @Author: 小爽帅到拖网速
 */
@Data
@ApiModel("文章、收藏数、点赞数")
public class ArticleDto implements Serializable {

    @ApiModelProperty(value = "图片列表")
    @TableField(exist = false)
    private List<String> pictureLinks;

    /**
     * 文章收藏数
     */
    private Integer collectionCount;

    /**
     * 文章点赞数
     */
    private Integer starCount;

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

    /**
     * 内容简介，没有简介默认截取正文前25个字
     */
    @TableField(value = "introduction")
    @ApiModelProperty(value = "内容简介，没有简介默认截取正文前25个字")
    private String introduction;

    /**
     * 图片链接
     */
    @TableField(value = "picture_link")
    @ApiModelProperty(value = "图片链接")
    private String pictureLink;

    /**
     * 正文
     */
    @TableField(value = "details")
    @ApiModelProperty(value = "正文")
    private String details;

    /**
     * 用户自定义标签，可多个
     */
    @TableField(value = "tag")
    @ApiModelProperty(value = "用户自定义标签，可多个")
    private String tag;

    /**
     * 视频链接
     */
    @TableField(value = "vedio_link")
    @ApiModelProperty(value = "视频链接")
    private String vedioLink;

    /**
     * 推荐月份
     */
    @TableField(value = "recommend_method")
    @ApiModelProperty(value = "推荐月份")
    private Integer recommendMethod;

    /**
     * 当前位置
     */
    @TableField(value = "location")
    @ApiModelProperty(value = "当前位置")
    private String location;

    /**
     * 产品  0普通产品 1 产品 default 0
     */
    @TableField(value = "is_product")
    @ApiModelProperty(value = "产品  0普通产品 1 产品 default 0")
    private Boolean isProduct;

    /**
     * 是否发布 0未发布 1 已发布 default 1
     */
    @TableField(value = "is_publish")
    @ApiModelProperty(value = "是否发布 0未发布 1 已发布 default 1")
    private Boolean isPublish;

    /**
     * 是否审核 0未审核  1已审核  default 0
     */
    @TableField(value = "is_examine")
    @ApiModelProperty(value = "是否审核 0未审核  1已审核  default 0")
    private Boolean isExamine;


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
