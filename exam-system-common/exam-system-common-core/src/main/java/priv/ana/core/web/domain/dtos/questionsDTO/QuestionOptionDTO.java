package priv.ana.core.web.domain.dtos.questionsDTO;

import lombok.Data;

/**
 * 题目选项 DTO
 */
@Data
public class QuestionOptionDTO {
    private String key;   // 选项键，如 A, B, C
    private String value; // 选项内容
}
