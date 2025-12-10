package priv.ana.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 提交答案请求 DTO
 */
@Data
public class SubmitAnswersRequestDTO {
    /**
     * 提交的题目答案列表
     */
    private List<UserAnswerDTO> answers;
    /**
     * 是否最终交卷 (true: 交卷, false: 实时保存)
     */
    private Boolean isFinalSubmit;
}
