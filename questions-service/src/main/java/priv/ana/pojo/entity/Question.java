package priv.ana.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import priv.ana.pojo.dto.QuestionInExamDTO;
import priv.ana.pojo.dto.QuestionOptionDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目实体
 */
@Data
@TableName(value = "questions",autoResultMap = true)
public class Question {
    /**
     * 题目ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 试卷ID
     */
    private Long examId;
    /**
     * 题干内容
     */
    private String content;
    /**
     * JSON字符串，存储正确答案 (如 "A" 或 "["A", "B"]")
     */
    private String correctAnswer;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * JSON字符串，存储选项列表。实际应用中是List<QuestionOptionDto>，为了数据库存取方便，存为JSON字符串。
     */
    @TableField(value="options",typeHandler = JacksonTypeHandler.class)
    private List<QuestionOptionDTO> options;
    /**
     * 分值
     */
    private Double score;
    /**
     * 最后更新时间
     */
//    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;
}
