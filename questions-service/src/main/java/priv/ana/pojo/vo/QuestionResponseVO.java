package priv.ana.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import priv.ana.pojo.dto.QuestionOptionDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通用题目响应 VO
 */
@Data
public class QuestionResponseVO {
    /**
     * 题目ID
     */
    private Long Id;
    /**
     * 题干内容
     */
    private String content;
    /**
     * 正确答案 (字符串或JSON数组字符串)
     */
    private String correctAnswer;
    /**
     * 选项列表
     */
    private List<QuestionOptionDTO> options;
    /**
     * 题目分值
     */
    private Double score;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}