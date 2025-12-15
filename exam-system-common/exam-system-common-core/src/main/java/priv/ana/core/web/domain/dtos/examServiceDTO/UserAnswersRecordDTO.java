package priv.ana.core.web.domain.dtos.examServiceDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 学生多题作答情况 DTO
 */
@Data
public class UserAnswersRecordDTO {
   @JsonProperty("userAnswerRecordDTOS")
   private List<UserAnswerRecordDTO> userAnswerRecordDTOS;
}
