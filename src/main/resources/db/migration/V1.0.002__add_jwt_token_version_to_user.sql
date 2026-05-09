-- 为 users 表添加 jwt_token_version 字段
ALTER TABLE `users`
    ADD COLUMN `jwt_token_version` INT NOT NULL DEFAULT 0 COMMENT 'JWT Token版本号，用于实现token失效机制';

-- 为现有用户设置默认版本号
UPDATE `users`
SET `jwt_token_version` = 0
WHERE `jwt_token_version` IS NULL;
