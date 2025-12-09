package priv.ana.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试成绩统计实体
 */
@Data
@TableName(value = "exam_grade_statistics", autoResultMap = true)
public class GradeStatistic {
    /**
     * 统计记录ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
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
    private Long numParticipants;
    /**
     * JSON字符串，存储分数分布，例如 { '0-60': 10, '60-70': 20, ... }
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String scoreDistribution;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
