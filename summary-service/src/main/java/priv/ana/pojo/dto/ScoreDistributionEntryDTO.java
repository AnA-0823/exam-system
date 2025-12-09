package priv.ana.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 分数分布条目 DTO
 */
@Data
public class ScoreDistributionEntryDTO {
    /**
     * 该区间人数
     */
    private Long count;
    /**
     * 分数区间，例如 '0-60'
     */
    private String range;
}
