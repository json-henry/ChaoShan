package com.chaoshan.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 呱呱
 * @date Created in 2022/5/15 15:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class UserDTO implements Serializable {
    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String accountid;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 头像链接
     */
    @ApiModelProperty(value = "头像链接")
    private String avatar;
    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    private String introduction;
    /**
     * 商家 0不是 1是 default 0
     */
    @ApiModelProperty(value = "商家 0不是 1是 default 0")
    private Boolean isBusiness;

    /**
     * 关注数
     */
    @ApiModelProperty(value = "关注数")
    private Integer followNum;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;
    /**
     * 预约报名
     */
    @ApiModelProperty(value = "预约报名")
    private Long signupNum;
    /**
     * 粉丝数
     */
    @ApiModelProperty(value = "粉丝数")
    private Integer fansNums;
    /**
     * 受邀用户数
     */
    @ApiModelProperty(value = "邀请用户数")
    private Integer invitationNum;


    private static final long serialVersionUID = 1L;
}