package priv.ana.pojo.vo;

import lombok.Data;
import priv.ana.core.web.domain.dtos.questionServiceDTO.QuestionOptionDTO;
import priv.ana.enums.AnswerResult;

import java.util.List;

/**
 * 单个题目答题详情 VO
 */
@Data
public class QuestionAnswerDetailVO {
    /**
     * 题干内容
     */
    private String content;
    /**
     * 正确答案
     */
    private String correctAnswer;
    /**
     * 该题目满分
     */
    private Double maxScore;
    /**
     * 题目选项 (对于选择题和判断题)
     */
    private List<QuestionOptionDTO> options;
    /**
     * 题目ID
     */
    private Long questionId;
    /**
     * 答题结果判断
     */
    private AnswerResult result;
    /**
     * 该题目得分
     */
    private Double score;
    /**
     * 学生提交的答案
     */
    private String userAnswer;
}