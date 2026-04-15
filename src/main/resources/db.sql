CREATE DATABASE IF NOT EXISTS mydatabase;
USE mydatabase;

-- 插入测试数据: 用户
-- 只在admin用户不存在时插入 -- 密码: admin123
INSERT INTO users (username, password_hash, nickname, status, online_status, roles, created_at, updated_at)
SELECT 'admin', '$2a$10$SzQy9i07BrMiKzuac.wrd.b1RPYuXc96ec155qbe52s7dZJWIXYwm', '管理员', 1, 0, '', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

-- 只在user用户不存在时插入 -- 密码: user123
INSERT INTO users (username, password_hash, nickname, status, online_status, roles, created_at, updated_at)
SELECT 'user', '$2a$10$/zcoe3wqJIOqLoKS/9UVNOPDQavZPmaie7YA2jWJe4hnhDVJJNcz6', '普通用户', 1, 0, '', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'user'
);

-- 插入测试数据: 好友请求
-- 插入测试数据: 好友关系
-- 插入测试数据: 会话


