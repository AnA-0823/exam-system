package priv.ana.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷列表响应 VO
 */
@Data
public class ExamSummaryResponseVO {
    /**
     * 考试时长
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
