package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @DATE: 2022/05/11 16:26
 * @Author: 小爽帅到拖网速
 */

@ApiModel(value = "com-chaoshan-entity-Article")
@Data
@TableName(value = "chaoshan.cs_article")
@Accessors(chain = true)
public class Article implements Serializable {

    @ApiModelProperty(value = "图片列表", hidden = true)
    @TableField(exist = false)
    private List<String> pictureLinks;

    @ApiModelProperty(value = "精选参考", notes = "点赞数+收藏数+浏览量", hidden = true)
    @TableField(exist = false)
    private Integer countMerge;

    @ApiModelProperty(value = "文章评论", hidden = true)
    @TableField(exist = false)
    private List<ArticleComment> articleComment;

    @ApiModelProperty(value = "点赞数")
    @TableField(exist = false)
    private Integer starCount;

    @ApiModelProperty(value = "收藏数")
    @TableField(exist = false)
    private Integer collectionCount;

    @ApiModelProperty(value = "文章用户", hidden = true)
    @TableField(exist = false)
    private User user;

    @ApiModelProperty(value = "文章标签", hidden = true)
    @TableField(exist = false)
    private List<String> tags;


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
    // @JsonIgnore // 忽略字段返回
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
    @TableField(value = "recommend_month")
    @ApiModelProperty(value = "推荐月份")
    private Integer recommendMonth;

    /**
     * 当前位置
     */
    @TableField(value = "location")
    @ApiModelProperty(value = "当前位置")
    private String location;

    /**
     * 管理员发布 0 否 1是 default 0
     */
    @TableField(value = "is_manager")
    @ApiModelProperty(value = "管理员发布 0 否 1是 default 0")
    private Boolean isManager;

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
     * 是否删除 0未删除 1已删除 default 0
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除 0未删除 1已删除 default 0")
    @TableLogic
    private Integer isDelete;

    /**
     * 类型 1饮食文化 2名人景点 3民间艺术 4潮玩攻略
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "类型 1饮食文化 2名人景点 3民间艺术 4潮玩攻略")
    private Integer type;

    /**
     * 浏览量 default 0
     */
    @TableField(value = "page_view")
    @ApiModelProperty(value = "浏览量 default 0")
    private Long pageView;

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

    private static final long serialVersionUID = 1L;
}