package priv.ana.core.web.domain.dtos.summaryServiceDTO;

import lombok.Data;

import java.util.List;

/**
 * 考试成绩统计创建请求 DTO
 */
@Data
public class GradeStatisticsCreateRequestDTO {
    /**
     * 关联的试卷ID
     */
    private Long examId;
    /**
     * 平均分
     */
    private Double averageScore;
    /**
     * 最高分
     */
    private Double highestScore;
    /**
     * 参与考试人数
     */
    private Integer numParticipants;
    /**
     * JSON字符串，存储分数分布
     */
    private List<ScoreDistributionEntryDTO> scoreDistribution;
}