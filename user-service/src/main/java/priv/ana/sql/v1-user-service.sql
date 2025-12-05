CREATE TABLE `t_user`
(
    `id`       INT AUTO_INCREMENT PRIMARY KEY,  -- 用户ID，唯一标识符
    `username` VARCHAR(50)  NOT NULL,           -- 用户名
    `password` VARCHAR(256) NOT NULL,           -- 密码，假设存储为加密格式
    `role`     TINYINT(1)   NOT NULL DEFAULT 1, -- 角色，枚举类型：1表示学生，2表示教师等
    `status`   TINYINT(1)   NOT NULL DEFAULT 1, -- 状态，枚举类型：1表示激活，0表示禁用
    UNIQUE KEY `unique_username` (`username`)   -- 保证用户名唯一
) ENGINE = InnoDBDEFAULT
  CHARSET = utf8mb4;
