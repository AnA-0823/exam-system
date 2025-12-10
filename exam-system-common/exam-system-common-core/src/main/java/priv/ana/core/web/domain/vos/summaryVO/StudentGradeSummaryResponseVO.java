package priv.ana.core.web.domain.vos.summaryVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentGradeSummaryResponseVO {
    /**
     * 试卷ID
     */
    private Long examId;
    /**
     * 试卷标题
     */
    private String examTitle;
    /**
     * 最终得分
     */
    private Double finalScore;
    /**
     * 考试记录ID
     */
    private Long recordId;
    /**
     * 交卷时间
     */
    private LocalDateTime submitTime;
}


