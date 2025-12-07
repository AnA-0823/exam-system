package priv.ana.pojo.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * ExamUpdateRequestDTO
 */
@Data
public class ExamUpdateRequestDTO {
    /**
     * 考试时长
     */
    private Long duration;
    /**
     * 新的题目列表 (会覆盖原有题目)
     */
    private List<QuestionInExamDTO> questions;
    /**
     * 考试开始时间 (可选修改)
     */
    private OffsetDateTime startTime;
    /**
     * 试卷标题 (可选修改)
     */
    private String title;
    /**
     * 试卷总分 (可选修改)
     */
    private Double totalScore;
}