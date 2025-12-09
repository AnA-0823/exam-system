package priv.ana.core.web.domain.dtos.examServiceDTO;

import lombok.Data;

/**
 * 学生单题作答情况 DTO
 */
@Data
public class UserAnswerRecordDTO {
    /**
     * 答题记录ID，主键
     */
    private Long id;
    /**
     * 关联的题目ID
     */
    private Long questionId;
    /**
     * 关联的考试记录ID
     */
    private Long recordId;
    /**
     * 该题目得分 (如果已评卷)
     */
    private Double score;
    /**
     * 学生答案 (JSON字符串或简单文本)
     */
    private String userAnswer;
}