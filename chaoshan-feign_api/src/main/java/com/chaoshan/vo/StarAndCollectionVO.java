package com.chaoshan.vo;

import com.chaoshan.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-14  23:22
 * @Description: 点赞数据展示通用模型
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StarAndCollectionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 文章id
     * 文章 或 话题文章
     */
    private Long articleId;

    /**
     * 点赞类型
     * 文章相关：  0文章点赞  1评论点赞（评论和回复都算评论）  2收藏
     * 话题文章相关  3 话题文章点赞 4评论点赞
     * 在commons中定义好了常量
     */
    private Integer type;

    /**
     * 发送点赞的用户信息
     */
    private User fromUser;

    /**
     * 被点赞的用户账号
     */
    private Long toAccountId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}
