package priv.ana.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ExamCreateRequestDTO
 */
@Data
public class ExamCreateRequestDTO {
    /**
     * 考试时长（分钟）
     */
    private Long duration;
    /**
     * 试卷包含的题目列表
     */
    private List<QuestionInExamDTO> questions;
    /**
     * 考试开始时间
     */
    private LocalDateTime startTime;
    /**
     * 试卷标题
     */
    private String title;
    /**
     * 试卷总分
     */
    private Double totalScore;
}
