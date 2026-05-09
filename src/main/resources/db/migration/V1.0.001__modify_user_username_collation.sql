-- noinspection SqlResolve
-- 修改 user 表 username 字段为大小写敏感 (utf8mb4_bin)
ALTER TABLE `users`
    MODIFY COLUMN `username` varchar(64)
        CHARACTER SET utf8mb4
            COLLATE utf8mb4_bin
        NOT NULL;

-- 重新创建唯一索引
DROP INDEX `idx_username` ON `users`;
ALTER TABLE `users` ADD UNIQUE INDEX `idx_username` (`username`);