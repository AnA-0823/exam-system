package priv.ana.pojo.vo;

import lombok.Data;
import priv.ana.core.web.domain.vos.quesitonsVO.QuestionDetailResponseVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 开始考试响应 VO
 */
@Data
public class StartExamResponseVO {
    /**
     * 试卷ID
     */
    private Long examId;
    /**
     * 生成的考试记录ID
     */
    private Long recordId;
    /**
     * 考试时长
     */
    private Long duration;
    /**
     * 试卷标题
     */
    private String examTitle;
    /**
     * 考试题目列表 (仅学生可见信息)
     */
    private List<QuestionDetailResponseVO> questions;
    /**
     * 考试开始时间
     */
    private LocalDateTime startTime;
}
