package priv.ana.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试记录实体
 */
@Data
@TableName("exam_records")
public class ExamRecord {
    /**
     * 考试记录ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 关联的试卷ID
     */
    private Long examId;
    /**
     * 参加考试的学生用户ID
     */
    private Long userId;
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
     * 记录创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    /**
     * 记录最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
