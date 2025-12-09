package priv.ana.core.web.domain.dtos.examServiceDTO;

import lombok.Data;

import java.util.List;

/**
 * 学生多题作答情况 DTO
 */
@Data
public class UserAnswersRecordDTO {
    List<UserAnswerRecordDTO> userAnswerRecordDTOS;
}
