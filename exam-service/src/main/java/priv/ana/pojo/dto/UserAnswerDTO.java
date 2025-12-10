package priv.ana.pojo.dto;

import lombok.Data;

/**
 * 学生答案 DTO
 */
@Data
public class UserAnswerDTO {
    /**
     * 题目ID
     */
    private Long questionId;
    /**
     * 学生提交的答案
     */
    private String userAnswer;
}
