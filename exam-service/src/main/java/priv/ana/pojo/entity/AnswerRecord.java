package priv.ana.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生答题详情实体
 */
@Data
@TableName(value = "answer_records",autoResultMap = true)
public class AnswerRecord {
    /**
     * 答题记录ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的题目ID
     */
    private Long questionId;
    /**
     * 关联的考试记录ID
     */
    private Long recordId;
    /**
     * 该题目得分 (如果已评卷)
     */
    private Double score;
    /**
     * 学生答案 (JSON字符串或简单文本)
     */
    private String userAnswer;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
