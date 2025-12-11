package priv.ana.pojo.vo;

import lombok.Data;
import priv.ana.pojo.dto.ScoreDistributionEntryDTO;

import java.util.List;

/**
 * 成绩统计响应 VO
 */
@Data
public class GradeStatisticsResponseVO {
    /**
     * 平均分
     */
    private Double averageScore;
    /**
     * 试卷ID
     */
    private Long examId;
    /**
     * 试卷标题
     */
    private String examTitle;
    /**
     * 最高分
     */
    private Double highestScore;
    /**
     * 参与考试人数
     */
    private Long numParticipants;
    /**
     * 分数分布列表
     */
    private List<ScoreDistributionEntryDTO> scoreDistribution;
}
