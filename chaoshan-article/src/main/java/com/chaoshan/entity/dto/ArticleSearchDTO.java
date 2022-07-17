package com.chaoshan.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @DATE: 2022/05/15 21:05
 * @Author: 小爽帅到拖网速
 */
@Data
@ApiModel("文章搜索DTO")
public class ArticleSearchDTO {

    /**
     * 点赞数+收藏数+浏览量
     */
    @ApiModelProperty(value = "点赞数+收藏数+浏览量")
    private Integer sum;

    /**
     * 文章点赞数
     */
    @ApiModelProperty(value = "文章点赞数")
    private Integer starCount;

    /**
     * 文章收藏数
     */
    @ApiModelProperty(value = "文章收藏数")
    private Integer collectionCount;

    /**
     * 文章作者账号
     */
    @ApiModelProperty(value = "文章作者账号")
    private String accountid;


    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
     * 内容简介，没有简介默认截取正文前25个字
     */
    @ApiModelProperty(value = "内容简介")
    private String introduction;

    /**
     * 图片链接
     */
    @ApiModelProperty(value = "图片链接")
    private String pictureLink;

    /**
     * 正文
     */
    @ApiModelProperty(value = "正文")
    private String details;

    /**
     * 标签，可多个
     */
    @ApiModelProperty(value = "标签")
    private String tag;

    /**
     * 推荐月份
     */
    @ApiModelProperty(value = "推荐月份")
    private Integer recommendMonth;

    /**
     * 当前位置
     */
    @ApiModelProperty(value = "当前位置")
    private String location;

    /**
     * 类型 1饮食文化 2名人景点 3民间艺术 4潮玩攻略
     */
    @ApiModelProperty(value = "类型")
    private Integer type;

    /**
     * 浏览量 default 0
     */
    @ApiModelProperty(value = "浏览量")
    private Integer pageView;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
