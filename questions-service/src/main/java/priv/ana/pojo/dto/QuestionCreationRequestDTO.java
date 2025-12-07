package priv.ana.pojo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 题目创建请求 DTO
 */
@Data
public class QuestionCreationRequestDTO {
    @NotNull
    private Long examId;        //所属试卷id
    @NotBlank
    private String content;     // 题目内容
    @NotNull
    private List<QuestionOptionDTO> options; // 题目选项
    @NotBlank
    private String correctAnswer;      // 参考答案
    @NotNull
    private Double score;       // 分值
}