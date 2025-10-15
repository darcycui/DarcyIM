CREATE DATABASE IF NOT EXISTS mydatabase;
USE mydatabase;

CREATE TABLE IF NOT EXISTS user
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    roles VARCHAR(100) NOT NULL
);

-- 插入测试数据
# INSERT INTO users (username, password, roles)
# VALUES ('admin', '$2a$10$3zHzb9K37b7X/.X1b0eP.eV5f4o1b3Q8Z7Q7Z7Q7Z7Q7Z7Q7Z7Q7',''), -- 密码: admin123
#        ('user', '$2a$10$3zHzb9K37b7X/.X1b0eP.eV5f4o1b3Q8Z7Q7Z7Q7Z7Q7Z7Q7Z7Q7',''); -- 密码: user123

-- 只在admin用户不存在时插入 -- 密码: admin123
INSERT INTO user (username, password, roles)
SELECT 'admin', '$2a$10$3zHzb9K37b7X/.X1b0eP.eV5f4o1b3Q8Z7Q7Z7Q7Z7Q7Z7Q7Z7Q7', ''
WHERE NOT EXISTS (
    SELECT 1 FROM user WHERE username = 'admin'
);

-- 只在user用户不存在时插入 -- 密码: user123
INSERT INTO user (username, password, roles)
SELECT 'user', '$2a$10$3zHzb9K37b7X/.X1b0eP.eV5f4o1b3Q8Z7Q7Z7Q7Z7Q7Z7Q7Z7Q7', ''
WHERE NOT EXISTS (
    SELECT 1 FROM user WHERE username = 'user'
);