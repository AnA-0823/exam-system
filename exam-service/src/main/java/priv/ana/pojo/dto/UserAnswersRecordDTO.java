package priv.ana.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 学生多题作答情况 DTO
 */
@Data
public class UserAnswersRecordDTO {
    private List<UserAnswerRecordDTO> answers;
}
