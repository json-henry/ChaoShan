package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 话题表
 *
 * @TableName cs_topic
 */
@Data
public class Topic implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 活动id
     */
    private Long activityid;

    /**
     * 标题
     */
    private String title;

    /**
     * 活动简介
     */
    private String introduction;

    /**
     * 浏览量
     */
    private Long viewCount;

    /**
     * 是否删除 0未删除 1已删除 default 0
     */
    private Integer isDelete;

    /**
     * 发布时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}