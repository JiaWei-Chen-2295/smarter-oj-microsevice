-- ================================
-- SmarterOJ 业务数据导入脚本
-- 按照依赖关系顺序导入各表数据
-- ================================

USE smarter_oj_db;

-- 禁用外键检查，加快导入速度
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 用户表（基础数据，无依赖）
SOURCE /docker-entrypoint-initdb.d/user.sql;

-- 2. 题目表（无依赖）
SOURCE /docker-entrypoint-initdb.d/question.sql;

-- 3. 题目单表（无依赖）
SOURCE /docker-entrypoint-initdb.d/question_set.sql;

-- 4. 题目单关联表（依赖 question_set 和 question）
SOURCE /docker-entrypoint-initdb.d/question_set_item.sql;

-- 5. 题目提交表（依赖 question 和 user）
SOURCE /docker-entrypoint-initdb.d/question_submit.sql;

-- 6. 帖子表（依赖 user）
SOURCE /docker-entrypoint-initdb.d/post.sql;

-- 7. 帖子收藏表（依赖 post 和 user）
SOURCE /docker-entrypoint-initdb.d/post_favour.sql;

-- 8. 帖子点赞表（依赖 post 和 user）
SOURCE /docker-entrypoint-initdb.d/post_thumb.sql;

-- 9. 房间表（依赖 user）
SOURCE /docker-entrypoint-initdb.d/room.sql;

-- 10. 用户房间关联表（依赖 user 和 room）
SOURCE /docker-entrypoint-initdb.d/user_room.sql;

-- 11. WebSocket 消息表（依赖 room 和 user）
SOURCE /docker-entrypoint-initdb.d/ws_message.sql;

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 输出导入完成信息
SELECT '✅ 业务数据导入完成！' AS Status;
