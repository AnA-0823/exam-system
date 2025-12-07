package priv.ana.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷详情响应 VO
 */
@Data
public class ExamDetailResponseVO {
    /**
     * 考试时长（分钟）
     */
    private Long duration;
    /**
     * 考试结束时间
     */
    private LocalDateTime endTime;
    /**
     * 试卷ID
     */
    private Long id;
    /**
     * 试卷包含的题目详情列表
     */
    private List<QuestionDetailResponseVO> questions;
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