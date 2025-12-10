-- -----------------------------------------------------
-- Table `exam_records` (考试记录表)
-- -----------------------------------------------------
-- 每次学生开始考试，都会生成一条记录
CREATE TABLE IF NOT EXISTS `exam_records` (
                                              `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '考试记录ID，主键',
                                              `exam_id` BIGINT NOT NULL COMMENT '关联的试卷ID',
                                              `user_id` BIGINT NOT NULL COMMENT '参加考试的学生用户ID',
                                              `exam_title` VARCHAR(255) NOT NULL COMMENT '试卷标题',
                                              `submit_time` TIMESTAMP NULL COMMENT '交卷时间 (如果已交卷)',
                                              `final_score` DOUBLE NULL COMMENT '最终得分 (如果已评卷)',
                                              `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                              `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
                                              PRIMARY KEY (`id`),
                                              INDEX `idx_user_id` (`user_id`),
                                              INDEX `idx_exam_id` (`exam_id`),
                                              INDEX `idx_final_score` (`final_score`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '考试记录表';

-- -----------------------------------------------------
-- Table `exam_record_answers` (考试记录答案表)
-- -----------------------------------------------------
-- 存储学生在每次考试中对每道题的作答情况
CREATE TABLE IF NOT EXISTS `answer_records` (
                                                `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '答题记录ID，主键',
                                                `record_id` BIGINT NOT NULL COMMENT '关联的考试记录ID',
                                                `question_id` BIGINT NOT NULL COMMENT '关联的题目ID',
                                                `user_answer` JSON NULL COMMENT '学生提交的答案 (JSON字符串或简单文本)',
                                                `score` DOUBLE NULL COMMENT '该题目得分 (如果已评卷)',
                                                `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                                                PRIMARY KEY (`id`),
                                                UNIQUE KEY `uk_record_question` (`record_id`, `question_id`) COMMENT '确保一次考试中对一道题目只有一份答案',
                                                INDEX `idx_record_id` (`record_id`),
                                                INDEX `idx_question_id` (`question_id`),
                                                FOREIGN KEY (`record_id`) REFERENCES `exam_records` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '考试记录答案表';
