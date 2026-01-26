-- ================================
-- SmarterOJ 业务数据库初始化脚本
-- 生成时间: 2026-01-26
-- 数据库: smarter_oj_db
-- ================================

USE smarter_oj_db;

-- ================================
-- 1. 用户表
-- ================================
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `unionId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开放平台id',
  `mpOpenId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公众号openId',
  `userName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_phone` (`phone`),
  KEY `idx_unionId` (`unionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';

-- ================================
-- 2. 题目表
-- ================================
CREATE TABLE IF NOT EXISTS `question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `tags` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签列表（json 数组）',
  `submitNum` int NOT NULL DEFAULT '0' COMMENT '提交总次数',
  `acceptedNum` int NOT NULL DEFAULT '0' COMMENT '通过人数',
  `judgeConfig` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '判题配置(JSON 对象)',
  `judgeCase` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '测试用例(JSON 数组)',
  `answer` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '问题参考答案',
  `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏数',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `codeTemplate` text COLLATE utf8mb4_unicode_ci COMMENT '代码模板(JSON对象)',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目';

-- ================================
-- 3. 题目单表
-- ================================
CREATE TABLE IF NOT EXISTS `question_set` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目单标题',
  `description` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '题目单描述',
  `tags` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签列表（json 数组）',
  `questionNum` int NOT NULL DEFAULT '0' COMMENT '题目数量',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏数',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目单';

-- ================================
-- 4. 题目单题目关联表
-- ================================
CREATE TABLE IF NOT EXISTS `question_set_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `questionSetId` bigint NOT NULL COMMENT '题目单id',
  `questionId` bigint NOT NULL COMMENT '题目id',
  `sortOrder` int NOT NULL DEFAULT '0' COMMENT '排序（越小越前）',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_question_set_question` (`questionSetId`,`questionId`),
  KEY `idx_questionSetId` (`questionSetId`),
  KEY `idx_questionId` (`questionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目单题目关联';

-- ================================
-- 5. 用户提交表
-- ================================
CREATE TABLE IF NOT EXISTS `question_submit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `questionId` bigint NOT NULL COMMENT '题目id',
  `userId` bigint NOT NULL COMMENT '提交的用户id',
  `language` varchar(125) NOT NULL COMMENT '提交的语言',
  `code` text NOT NULL COMMENT '用户提交的代码',
  `status` int NOT NULL COMMENT '判题状态0-待判题 1-判题中 2-成功 3-失败',
  `judgeInfo` text NOT NULL COMMENT '判题信息',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `outputResult` text COMMENT '代码的运行结果',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_questionId` (`questionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户提交表';

-- ================================
-- 6. 帖子表
-- ================================
CREATE TABLE IF NOT EXISTS `post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `tags` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签列表（json 数组）',
  `thumbNum` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `favourNum` int NOT NULL DEFAULT '0' COMMENT '收藏数',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子';

-- ================================
-- 7. 帖子收藏表
-- ================================
CREATE TABLE IF NOT EXISTS `post_favour` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint NOT NULL COMMENT '帖子 id',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_postId` (`postId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子收藏';

-- ================================
-- 8. 帖子点赞表
-- ================================
CREATE TABLE IF NOT EXISTS `post_thumb` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint NOT NULL COMMENT '帖子 id',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_postId` (`postId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子点赞';

-- ================================
-- 9. 房间表
-- ================================
CREATE TABLE IF NOT EXISTS `room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_name` varchar(256) NOT NULL COMMENT '队伍名称',
  `description` varchar(1024) NOT NULL COMMENT '房间描述',
  `mate_num` tinyint NOT NULL COMMENT '房间最大人数',
  `user_id` bigint NOT NULL COMMENT '房间队长的用户id',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '房间状态0-公开 1-非公开',
  `password` varchar(256) DEFAULT NULL COMMENT '密码 如果是公开的就是 NULL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '房间创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='房间表';

-- ================================
-- 10. 用户_房间表
-- ================================
CREATE TABLE IF NOT EXISTS `user_room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '房间的用户id',
  `room_id` bigint NOT NULL COMMENT '队伍id',
  `join_time` datetime DEFAULT NULL COMMENT '加入房间时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '条目创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户_房间表';

-- ================================
-- 11. WebSocket消息表
-- ================================
CREATE TABLE IF NOT EXISTS `ws_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '发送者ID',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `message_id` bigint NOT NULL COMMENT '消息唯一ID',
  `content` text NOT NULL COMMENT '完整消息体（含type/content/userId等）',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0-pending 1-sent 2-deleted',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_msg_id` (`message_id`),
  KEY `idx_room_time` (`room_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ================================
-- 建表语句执行完成
-- ================================
