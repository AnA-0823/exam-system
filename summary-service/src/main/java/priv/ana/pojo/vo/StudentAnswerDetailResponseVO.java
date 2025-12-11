package priv.ana.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学生答题详情响应 VO - 教师/学生查看
 */
@Data
public class StudentAnswerDetailResponseVO {
    /**
     * 每道题目的答题详情
     */
    private List<QuestionAnswerDetailVO> answers;
    /**
     * 试卷ID
     */
    private Long examId;
    /**
     * 试卷标题
     */
    private String examTitle;
    /**
     * 学生最终得分
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
