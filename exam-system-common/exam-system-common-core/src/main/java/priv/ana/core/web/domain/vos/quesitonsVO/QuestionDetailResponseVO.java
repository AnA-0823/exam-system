package priv.ana.core.web.domain.vos.quesitonsVO;

import lombok.Data;
import priv.ana.core.web.domain.dtos.questionsDTO.QuestionOptionDTO;

import java.util.List;

/**
 * 题目详情响应 VO - 用于展示给学生
 */
@Data
public class QuestionDetailResponseVO {
    /**
     * 题干内容
     */
    private String content;
    /**
     * 选项列表
     */
    private List<QuestionOptionDTO> options;
    /**
     * 题目ID
     */
    private Long id;
    /**
     * 题目分值
     */
    private Double score;
}