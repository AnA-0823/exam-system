CREATE TABLE IF NOT EXISTS `exam_grade_statistics` (
                                                       `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '统计ID',
                                                       `exam_id` BIGINT NOT NULL UNIQUE COMMENT '关联的试卷ID',
                                                       `num_participants` INT NOT NULL DEFAULT 0 COMMENT '参与考试人数',
                                                       `average_score` DOUBLE NOT NULL DEFAULT 0.0 COMMENT '平均得分',
                                                       `highest_score` DOUBLE NOT NULL DEFAULT 0.0 COMMENT '最高得分',
                                                       `score_distribution_json` JSON NULL COMMENT '分数分布区间统计（JSON格式，例如：[{"range":"0-60","count":10},...]）',
                                                       `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                       `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                       PRIMARY KEY (`id`),
                                                       INDEX `idx_egs_exam_id` (`exam_id`) -- 用于查询优化
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '考试成绩统计表';
