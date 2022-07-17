package com.chaoshan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 呱呱
 * @date Created in 2022/5/17 9:38
 */
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "MessageDTO", description = "消息传输数据模型")
public class MessageDTO implements Serializable {

    /**
     * 发送方用户名
     */
    @ApiModelProperty(value = "发送方用户名")
    private String username;
    /**
     * 发送方头像链接
     */
    @ApiModelProperty(value = "发送方头像链接")
    private String avatar;
    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Long articleid;
    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;
    /**
     * 文章图片链接
     */
    @ApiModelProperty(value = "文章图片链接")
    private String pictureLink;
    /**
     * 消息类型 消息类型 0 活动 1 话题 2 文章评论 3文章回复 4 文章点赞
     * 5话题评论 6 话题回复 7话题点赞 8收藏 9关注（新增）
     **/
    @ApiModelProperty(value = "消息类型 0 活动 1 话题 2 文章评论 3文章回复" +
            " 4 文章点赞  5话题评论 6 话题回复 7话题点赞 8收藏 9关注（新增）")
    private Integer messageType;

    /**
     * 消息内容
     */
    @TableField(value = "message")
    @ApiModelProperty(value = "消息内容")
    private String message;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    public MessageDTO(String username, String avatar, Long articleid, String title, String pictureLink,
                      Integer messageType, LocalDateTime createTime) {
        this.username = username;
        this.avatar = avatar;
        this.articleid = articleid;
        this.title = title;
        this.pictureLink = pictureLink;
        this.messageType = messageType;
        this.createTime = createTime;
    }
}
