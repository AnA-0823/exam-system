package priv.ana.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 题目更新请求 DTO
 */
@Data
public class QuestionUpdateRequestDTO {
    /**
     * 所属试卷id
     */
    @NotNull(message = "所属试卷id不能为空")
    private Long examId;
    /**
     * 题干内容 (可选修改)
     */
    private String content;
    /**
     * 正确答案 (可选修改)
     */
    private String correctAnswer;
    /**
     * 选项列表 (可选修改)
     */
    private List<QuestionOptionDTO> options;
    /**
     * 分值 (可选修改)
     */
    private Double score;
}

