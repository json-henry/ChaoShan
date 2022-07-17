/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : chaoshan

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 13/05/2022 11:17:50
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cs_activity
-- ----------------------------
DROP TABLE IF EXISTS `cs_activity`;
CREATE TABLE `cs_activity`
(
    `id`           bigint(20) NOT NULL COMMENT '主键',
    `title`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
    `picture_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '活动图链接',
    `details`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '正文',
    `is_delete`    tinyint(1) NULL DEFAULT NULL COMMENT '是否删除 0未删除 1已删除  default 0',
    `start_time`   datetime(0) NULL DEFAULT NULL COMMENT '活动开始时间',
    `end_time`     datetime(0) NULL DEFAULT NULL COMMENT '活动结束时间',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_by`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_by`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_activity
-- ----------------------------

-- ----------------------------
-- Table structure for cs_activity_appointment
-- ----------------------------
DROP TABLE IF EXISTS `cs_activity_appointment`;
CREATE TABLE `cs_activity_appointment`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `activityid`  bigint(20) NULL DEFAULT NULL COMMENT '活动id',
    `accountid`   bigint(20) NULL DEFAULT NULL COMMENT '用户账号',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '活动预约表\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_activity_appointment
-- ----------------------------

-- ----------------------------
-- Table structure for cs_activity_video
-- ----------------------------
DROP TABLE IF EXISTS `cs_activity_video`;
CREATE TABLE `cs_activity_video`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `activityid`  bigint(20) NULL DEFAULT NULL COMMENT '活动id',
    `videoid`     bigint(20) NULL DEFAULT NULL COMMENT '视频号id',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0 未删除 1已删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_by`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '活动视频表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_activity_video
-- ----------------------------

-- ----------------------------
-- Table structure for cs_article
-- ----------------------------
DROP TABLE IF EXISTS `cs_article`;
CREATE TABLE `cs_article`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文章标题',
    `introduction`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容简介，没有简介默认截取正文前25个字',
    `picture_link`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片链接',
    `details`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '正文',
    `tag`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户自定义标签，可多个',
    `vedio_link`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '视频链接',
    `recommend_month` int(10) NULL DEFAULT NULL COMMENT '推荐月份',
    `location`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '当前位置',
    `is_manager`      tinyint(1) NULL DEFAULT 0 COMMENT '管理员发布 0 否 1是 default 0',
    `is_product`      tinyint(1) NULL DEFAULT 0 COMMENT '产品  0普通产品 1 产品 default 0',
    `is_publish`      tinyint(1) NULL DEFAULT 0 COMMENT '是否发布 0未发布 1 已发布 default 1',
    `is_examine`      tinyint(1) NULL DEFAULT 0 COMMENT '是否审核 0未审核  1已审核  default 0',
    `is_delete`       int(2) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除 default 0',
    `type`            tinyint(1) NULL DEFAULT NULL COMMENT '类型 1饮食文化 2名人景点 3民间艺术 4潮玩攻略',
    `page_view`       bigint(20) NULL DEFAULT 0 COMMENT '浏览量 default 0',
    `create_time`     datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_by`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_by`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_article
-- ----------------------------
INSERT INTO `cs_article`
VALUES (1, '1', '11', '1', '1', '1', '1', 1, '1', 0, 0, 0, 0, 0, NULL, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for cs_article_attraction
-- ----------------------------
DROP TABLE IF EXISTS `cs_article_attraction`;
CREATE TABLE `cs_article_attraction`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `scene`       enum('室内','室外') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '场景 室内或室外',
    `score`       decimal(2, 0) NULL DEFAULT NULL COMMENT '评分',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '名人景点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_article_attraction
-- ----------------------------

-- ----------------------------
-- Table structure for cs_article_collection
-- ----------------------------
DROP TABLE IF EXISTS `cs_article_collection`;
CREATE TABLE `cs_article_collection`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `accountid`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送方账号',
    `articleid`   bigint(20) NULL DEFAULT NULL COMMENT '文章id',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除  default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文章收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_article_collection
-- ----------------------------
INSERT INTO `cs_article_collection`
VALUES (1, '1', 1, 0, NULL, NULL);

-- ----------------------------
-- Table structure for cs_article_comment
-- ----------------------------
DROP TABLE IF EXISTS `cs_article_comment`;
CREATE TABLE `cs_article_comment`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `accountid`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送方账号',
    `articleid`   bigint(20) NULL DEFAULT NULL COMMENT '文章id',
    `message`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息内容',
    `star`        int(20) NULL DEFAULT NULL COMMENT '点赞数',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文章评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_article_comment
-- ----------------------------

-- ----------------------------
-- Table structure for cs_article_comment_star
-- ----------------------------
DROP TABLE IF EXISTS `cs_article_comment_star`;
CREATE TABLE `cs_article_comment_star`
(
    `id`          bigint(20) NOT NULL COMMENT '主键',
    `articleid`   bigint(20) NULL DEFAULT NULL COMMENT '文章表id',
    `comment_id`  bigint(20) NULL DEFAULT NULL COMMENT '评论id',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0 否 1 是  default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文章评论点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_article_comment_star
-- ----------------------------

-- ----------------------------
-- Table structure for cs_article_reply
-- ----------------------------
DROP TABLE IF EXISTS `cs_article_reply`;
CREATE TABLE `cs_article_reply`
(
    `id`           bigint(20) NOT NULL COMMENT '主键',
    `comment_id`   bigint(20) NOT NULL COMMENT '评论id',
    `accountid`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '回复用户id',
    `to_accountid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '目标用户id',
    `content`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '评论内容',
    `is_delete`    tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0 未删除 1已删除 default 0',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文章评论回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_article_reply
-- ----------------------------

-- ----------------------------
-- Table structure for cs_article_star
-- ----------------------------
DROP TABLE IF EXISTS `cs_article_star`;
CREATE TABLE `cs_article_star`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `accountid`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送方账号',
    `articleid`   bigint(20) NULL DEFAULT NULL COMMENT '文章id',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0已删除 1未删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文章点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_article_star
-- ----------------------------
INSERT INTO `cs_article_star`
VALUES (1, '1', 1, 0, NULL, NULL);

-- ----------------------------
-- Table structure for cs_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `cs_article_tag`;
CREATE TABLE `cs_article_tag`
(
    `id`          bigint(20) NOT NULL COMMENT '主键',
    `tag_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签名',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '删除 0 未删除 1已删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文章标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for cs_fans
-- ----------------------------
DROP TABLE IF EXISTS `cs_fans`;
CREATE TABLE `cs_fans`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `accountid`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关注用户账号',
    `accountided` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '粉丝用户账号',
    `is_delete`   tinyint(1) NULL DEFAULT NULL COMMENT '是否删除 0未删除 1已删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_fans
-- ----------------------------

-- ----------------------------
-- Table structure for cs_openscenic
-- ----------------------------
DROP TABLE IF EXISTS `cs_openscenic`;
CREATE TABLE `cs_openscenic`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `picture_link`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '景区图',
    `accessible_number` int(20) NULL DEFAULT NULL COMMENT '准许进入人数',
    `is_delete`         tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除 default 0',
    `open_time`         datetime(0) NULL DEFAULT NULL COMMENT '开放时间',
    `end_time`          datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
    `create_time`       datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `create_by`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `update_time`       datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_by`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '开放景区表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_openscenic
-- ----------------------------

-- ----------------------------
-- Table structure for cs_openscenic_signup
-- ----------------------------
DROP TABLE IF EXISTS `cs_openscenic_signup`;
CREATE TABLE `cs_openscenic_signup`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `openscenicid`       bigint(20) NULL DEFAULT NULL COMMENT '开发景区id',
    `accountid`          bigint(20) NULL DEFAULT NULL COMMENT '用户账号',
    `adminssion_time`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '入场时间段',
    `current_position`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '当前位置',
    `is_contact_history` tinyint(1) NULL DEFAULT 0 COMMENT '是否有14天内接触史 0 否 1 是  default 0',
    `is_health_code`     tinyint(1) NULL DEFAULT 1 COMMENT '是否绿码 0 否 1 是 default 1',
    `is_health`          tinyint(1) NULL DEFAULT 1 COMMENT '是否健康 0 否 1 是 default 1',
    `is_true`            tinyint(1) NULL DEFAULT 1 COMMENT '是否属实 0 否 1 是 default 1',
    `is_delete`          tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0 未删除 1已删除 default 0',
    `create_time`        datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`        datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '开放景区报名表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_openscenic_signup
-- ----------------------------

-- ----------------------------
-- Table structure for cs_search_keyword
-- ----------------------------
DROP TABLE IF EXISTS `cs_search_keyword`;
CREATE TABLE `cs_search_keyword`
(
    `id`          bigint(20) NOT NULL COMMENT '主键',
    `keyword`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关键字',
    `search_type` int(10) NULL DEFAULT NULL COMMENT '搜索类型 1 文章 2 话题',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '删除 0 未删除 1 已删除  default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '搜索关键字记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_search_keyword
-- ----------------------------

-- ----------------------------
-- Table structure for cs_search_record
-- ----------------------------
DROP TABLE IF EXISTS `cs_search_record`;
CREATE TABLE `cs_search_record`
(
    `id`          bigint(20) NOT NULL COMMENT '主键',
    `accountid`   bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    `record`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '搜索记录',
    `search_type` int(10) NULL DEFAULT NULL COMMENT '搜索类型',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '删除 0 未删除 1 已删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '搜索历史记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_search_record
-- ----------------------------

-- ----------------------------
-- Table structure for cs_seller
-- ----------------------------
DROP TABLE IF EXISTS `cs_seller`;
CREATE TABLE `cs_seller`
(
    `id`                 bigint(20) NOT NULL COMMENT '主键',
    `accountid`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户账号',
    `picture_credential` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片凭证',
    `is_check`           tinyint(1) NULL DEFAULT 0 COMMENT '是否审核 0 未审核 1已审核 default 0',
    `is_delete`          tinyint(1) NULL DEFAULT NULL COMMENT '是否删除 0 已删除 1 未删除 default 0',
    `create_time`        datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`        datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '商家认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_seller
-- ----------------------------

-- ----------------------------
-- Table structure for cs_topic
-- ----------------------------
DROP TABLE IF EXISTS `cs_topic`;
CREATE TABLE `cs_topic`
(
    `id`           bigint(20) UNSIGNED ZEROFILL NOT NULL COMMENT '主键',
    `activityid`   bigint(20) NULL DEFAULT NULL COMMENT '活动id',
    `title`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
    `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '活动简介',
    `hot_rank`     int(20) NULL DEFAULT NULL COMMENT '热点排名',
    `is_delete`    tinyint(1) NULL DEFAULT NULL COMMENT '是否删除 0未删除 1已删除 default 0',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
    `create_by`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `end_time`     datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `update_by`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '话题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_topic
-- ----------------------------

-- ----------------------------
-- Table structure for cs_topic_article
-- ----------------------------
DROP TABLE IF EXISTS `cs_topic_article`;
CREATE TABLE `cs_topic_article`
(
    `id`           bigint(20) NOT NULL COMMENT '主键',
    `topic_id`     bigint(20) NULL DEFAULT NULL COMMENT '话题id',
    `accountid`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户账号',
    `star`         bigint(20) NULL DEFAULT NULL COMMENT '点赞数',
    `content`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '评论内容',
    `picture_link` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '图片链接',
    `is_delete`    tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0 未删除 1已删除 default 0',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '活动文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_topic_article
-- ----------------------------

-- ----------------------------
-- Table structure for cs_topic_article_comment
-- ----------------------------
DROP TABLE IF EXISTS `cs_topic_article_comment`;
CREATE TABLE `cs_topic_article_comment`
(
    `id`            bigint(20) NOT NULL COMMENT '主键',
    `send_accoutid` bigint(20) NULL DEFAULT NULL COMMENT '发送方账号',
    `article_id`    bigint(20) NULL DEFAULT NULL COMMENT '话题文章id',
    `message`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息内容',
    `star`          bigint(20) NULL DEFAULT NULL COMMENT '点赞数',
    `is_delete`     tinyint(1) NULL DEFAULT 0 COMMENT '删除 0 未删除 1 已删除 default 0',
    `create_time`   datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '话题文章评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_topic_article_comment
-- ----------------------------

-- ----------------------------
-- Table structure for cs_topic_article_reply
-- ----------------------------
DROP TABLE IF EXISTS `cs_topic_article_reply`;
CREATE TABLE `cs_topic_article_reply`
(
    `id`                 bigint(20) NOT NULL COMMENT '主键',
    `article_comment_id` bigint(20) NULL DEFAULT NULL COMMENT '话题评论id  (朋友圈评论的回复)',
    `accountid`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '回复用户id',
    `to_accountid`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '目标用户id',
    `content`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '评论内容',
    `is_delete`          tinyint(1) NULL DEFAULT 0 COMMENT '删除 0 未删除 1 已删除  default 0',
    `create_time`        datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`        datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '话题下的文章评论的回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_topic_article_reply
-- ----------------------------

-- ----------------------------
-- Table structure for cs_topic_article_star
-- ----------------------------
DROP TABLE IF EXISTS `cs_topic_article_star`;
CREATE TABLE `cs_topic_article_star`
(
    `id`             bigint(20) NOT NULL COMMENT '主键',
    `send_accountid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送方账号',
    `article_id`     bigint(20) NULL DEFAULT NULL COMMENT '话题文章id',
    `is_delete`      tinyint(1) NULL DEFAULT 0 COMMENT '删除 0 未删除 1已删除 default 0',
    `create_time`    datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '话题文章点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_topic_article_star
-- ----------------------------

-- ----------------------------
-- Table structure for cs_user
-- ----------------------------
DROP TABLE IF EXISTS `cs_user`;
CREATE TABLE `cs_user`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `accountid`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户账号',
    `username`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
    `password`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户密码（加密）',
    `phone`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系电话',
    `gender`          tinyint(1) NULL DEFAULT 1 COMMENT '性别  0女 1男 default 1',
    `invitation_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邀请码',
    `avatar`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像链接',
    `introduction`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '简介',
    `is_business`     tinyint(1) NULL DEFAULT 0 COMMENT '商家 0不是 1是 default 0',
    `is_delete`       tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除 default 0',
    `create_time`     datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`     datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1524253752216301570 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_user
-- ----------------------------
INSERT INTO `cs_user`
VALUES (1, '19832932484', 'zs', '$2a$10$r6lx2JGj0.qrsfgM27edFeDF1fyF3mowYLmwJrsV0gTVzEkmHQKaW', NULL, 1, NULL, NULL,
        NULL, 0, 0, NULL, NULL);
INSERT INTO `cs_user`
VALUES (1521680092496269314, NULL, '19832932484', '$2a$10$JkZz9mg090XXa1vtrJG0BefYQH8z2nZtJgDpls88YtMwaYll.qPmu', NULL,
        1, NULL, NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `cs_user`
VALUES (1521681867131490306, NULL, 'zs', '$2a$10$F8AmMpuuz.d3/JTEvUc7lexL/R6YM1PWWZ7A3KNZL9NDLW1mX671e', NULL, 1, NULL,
        NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `cs_user`
VALUES (1521682130756079618, NULL, '13539642132', '$2a$10$8tO0k6POuc9M/TcpVoSTy.ci6L5vAMna4DTODEWFqhVfiZQ7SY/8G', NULL,
        1, NULL, NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `cs_user`
VALUES (1521769216750354433, NULL, 'zs', '$2a$10$fpB6shas2DRIRS/pya4ff.82MJMEYs/oXN4t7d79AxsxOwM.eg70O', NULL, 1, NULL,
        NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `cs_user`
VALUES (1524205861231558658, NULL, '10086', '$2a$10$XE7j0KNT2NXx/g9YZtlOTeDKoc7ySdNOTHFJJeu2FvEKqMbC8EO0C', NULL, 1,
        NULL, NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `cs_user`
VALUES (1524207304357412865, '小爽帅到拖网速', NULL, '$2a$10$jUKBZMhV6xUegMk6Sdk/wet2utxtK0.iVqkxvwwVOi3yq4pK4Keb.', NULL, 1,
        NULL, NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `cs_user`
VALUES (1524207730720997377, '郑爽', NULL, '$2a$10$0CZPJAeiUm4UkY7IHZqJQe..UXNcgHqSXMnVMCnefoTEzorFj3yXW', NULL, 1, NULL,
        NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `cs_user`
VALUES (1524210656539652098, '蒙奇D路飞', NULL, '$2a$10$kyOZJn4RsDbfZpqcDY/LUOgiyLvPFX93hmNniU5IngP7waXQwyYTK', NULL, 1,
        NULL, NULL, NULL, 0, 1, '2022-05-11 02:11:54', '2022-05-11 02:11:54');
INSERT INTO `cs_user`
VALUES (1524253752216301569, '蒙奇D路飞', NULL, '$2a$10$sB.uvXeyDJygs//P8uxYmOCgi9fPDoccuDY0e4phNHt8hO4EufR/W', NULL, 1,
        NULL, NULL, NULL, 0, 0, '2022-05-11 05:03:08', '2022-05-11 05:03:08');

-- ----------------------------
-- Table structure for cs_user_article
-- ----------------------------
DROP TABLE IF EXISTS `cs_user_article`;
CREATE TABLE `cs_user_article`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `accountid`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户账号',
    `articleid`   bigint(20) NULL DEFAULT NULL COMMENT '文章id',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0未删除 1已删除 default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_user_article
-- ----------------------------

-- ----------------------------
-- Table structure for cs_user_invitation
-- ----------------------------
DROP TABLE IF EXISTS `cs_user_invitation`;
CREATE TABLE `cs_user_invitation`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `accountid`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邀请方账号',
    `accountided` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '被邀请方账号',
    `is_delete`   tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0 未删除  1 已删除  default 0',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户邀请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_user_invitation
-- ----------------------------

-- ----------------------------
-- Table structure for cs_user_message
-- ----------------------------
DROP TABLE IF EXISTS `cs_user_message`;
CREATE TABLE `cs_user_message`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `send_accountid`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送方账号',
    `receive_accountid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '接收方账号',
    `message`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息内容',
    `is_read`           tinyint(1) NULL DEFAULT 0 COMMENT '是否已读 0未读取 1已读取 default 0',
    `create_time`       datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`       datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `is_delete`         tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除 0 未删除 1已删除 default 0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_user_message
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端标 识',
    `resource_ids`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接入资源列表',
    `client_secret`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端秘钥',
    `scope`                   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `authorized_grant_types`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `web_server_redirect_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `authorities`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `access_token_validity`   int(11) NULL DEFAULT NULL,
    `refresh_token_validity`  int(11) NULL DEFAULT NULL,
    `additional_information`  longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
    `create_time`             timestamp(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP (0),
    `archived`                tinyint(4) NULL DEFAULT NULL,
    `trusted`                 tinyint(4) NULL DEFAULT NULL,
    `autoapprove`             varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接入客户端信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details`
VALUES ('c1', 'res1', '$2a$10$DaXy0Hb/6I9LuUFV/c0SMOlgB8cne19TJDC4J1Ypg20r8Hezxu6JW', 'ROLE_ADMIN,ROLE_USER,ROLE_API',
        'client_credentials,password,authorization_code,implicit,refresh_token', 'http://www.baidu.com', NULL, 7200,
        259200, NULL, '2022-02-15 22:50:28', 0, 0, 'false');
INSERT INTO `oauth_client_details`
VALUES ('c2', 'res2', '$2a$10$DaXy0Hb/6I9LuUFV/c0SMOlgB8cne19TJDC4J1Ypg20r8Hezxu6JW', 'ROLE_API',
        'client_credentials,password,authorization_code,implicit,refresh_token', 'http://www.baidu.com', NULL, 31536000,
        2592000, NULL, '2022-02-15 22:50:30', 0, 0, 'false');

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `create_time`    timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `code`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `authentication` blob NULL,
    INDEX            `code_index`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'oauth授权码存储表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of oauth_code
-- ----------------------------

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `code`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限标识符',
    `description` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `url`         varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求地址',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission`
VALUES ('1', 'p1', '测试资源 1', '/r/r1');
INSERT INTO `t_permission`
VALUES ('2', 'p3', '测试资源2', '/r/r2');
INSERT INTO `t_permission`
VALUES ('3', 'basic_user', '普通用户', '/basic');
INSERT INTO `t_permission`
VALUES ('4', 'seller_user', '商家用户', '/seller');
INSERT INTO `t_permission`
VALUES ('5', 'admin', '系统管理员', '/admin');
INSERT INTO `t_permission`
VALUES ('6', 'test_user', '测试资源', '/**');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `role_name`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `create_time` datetime(0) NULL DEFAULT NULL,
    `update_time` datetime(0) NULL DEFAULT NULL,
    `status`      char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role`
VALUES ('1', '管理员', NULL, NULL, NULL, '');
INSERT INTO `t_role`
VALUES ('2', '普通用户', '登录后的用户默认为普通用户，可以对文章进行点赞评论收藏等等基本操作', NULL, NULL, NULL);
INSERT INTO `t_role`
VALUES ('3', '商家用户', '完成商家认证的用户，可以发布商家产品', NULL, NULL, NULL);
INSERT INTO `t_role`
VALUES ('4', '测试用户', '拥有普通用户，商家用户权限', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`
(
    `role_id`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission`
VALUES ('1', '1');
INSERT INTO `t_role_permission`
VALUES ('1', '2');
INSERT INTO `t_role_permission`
VALUES ('1', '3');
INSERT INTO `t_role_permission`
VALUES ('1', '4');
INSERT INTO `t_role_permission`
VALUES ('1', '5');
INSERT INTO `t_role_permission`
VALUES ('2', '3');
INSERT INTO `t_role_permission`
VALUES ('3', '4');
INSERT INTO `t_role_permission`
VALUES ('4', '6');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`
(
    `user_id`     bigint(32) NOT NULL,
    `role_id`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP (0),
    `creator`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role`
VALUES (1, '4', '2022-05-11 14:27:11', NULL);
INSERT INTO `t_user_role`
VALUES (4, '2', '2022-05-11 14:09:50', NULL);
INSERT INTO `t_user_role`
VALUES (1521682130756079618, '2', '2022-05-11 14:09:51', NULL);
INSERT INTO `t_user_role`
VALUES (1524205861231558658, '2', '2022-05-11 14:09:52', NULL);
INSERT INTO `t_user_role`
VALUES (1524207304357412865, '2', '2022-05-11 14:09:52', NULL);
INSERT INTO `t_user_role`
VALUES (1524207730720997377, '2', '2022-05-11 14:09:53', NULL);
INSERT INTO `t_user_role`
VALUES (1524210656539652098, '2', '2022-05-11 14:09:54', NULL);
INSERT INTO `t_user_role`
VALUES (1524253752216301569, '2', '2022-05-11 14:09:56', NULL);

SET
FOREIGN_KEY_CHECKS = 1;
