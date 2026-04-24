-- 修改 user 表 username 字段为大小写敏感 (utf8mb4_bin)
ALTER TABLE `users`
    MODIFY COLUMN `username` varchar(64)
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_bin
    NOT NULL;

-- 重建唯一索引（兼容旧版 MySQL）
-- 先尝试删除索引（如果存在）
SET @dbname = DATABASE();
SET @tablename = 'users';
SET @indexname = 'idx_username';
SET @preparedStatement = (SELECT IF(
 COUNT(*) > 0,
 CONCAT('DROP INDEX `', @indexname, '` ON `', @tablename, '`'),
 'SELECT "Index does not exist"'
 )
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = @tablename
    AND INDEX_NAME = @indexname);
PREPARE alterIfExists FROM @preparedStatement;
EXECUTE alterIfExists;
DEALLOCATE PREPARE alterIfExists;

-- 重新创建唯一索引
ALTER TABLE `users` ADD UNIQUE INDEX `idx_username` (`username`);