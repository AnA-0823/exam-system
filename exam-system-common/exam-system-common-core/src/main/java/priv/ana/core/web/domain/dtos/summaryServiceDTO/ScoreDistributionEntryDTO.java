package priv.ana.core.web.domain.dtos.summaryServiceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分数分布条目 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDistributionEntryDTO {
    /**
     * 该区间人数
     */
    private Integer count;
    /**
     * 区间下界
     */
    private Integer lowerBound;
    /**
     * 区间上界
     */
    private Integer upperBound;
}
