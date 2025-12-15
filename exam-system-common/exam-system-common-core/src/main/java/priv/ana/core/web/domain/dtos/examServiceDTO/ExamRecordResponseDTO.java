package priv.ana.core.web.domain.dtos.examServiceDTO;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试详情响应 DTO（用于成绩统计
 */
@Data
public class ExamRecordResponseDTO {
    /**
     * 关联的试卷ID
     */
    private Long examId;
    /**
     * 试卷标题
     */
    private String examTitle;
    /**
     * 最终得分 (如果已评卷)
     */
    private Double finalScore;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 考试记录ID，主键
     */
    private Long id;
}
