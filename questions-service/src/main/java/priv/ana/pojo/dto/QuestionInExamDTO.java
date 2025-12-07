package priv.ana.pojo.dto;
import lombok.Data;

import java.util.List;

/**
 * 试卷内题目 DTO
 */
@Data
public class QuestionInExamDTO {
    /**
     * 题干内容
     */
    private String content;
    /**
     * 正确答案 (字符串或JSON数组字符串，如 'A' 或 '["A", "B"]')
     */
    private String correctAnswer;
    /**
     * 选项列表 (对于选择题和判断题)
     */
    private List<QuestionOptionDTO> options;
    /**
     * 题目分值
     */
    private Double score;
}
