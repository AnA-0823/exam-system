package priv.ana.service;

import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.vos.summaryVO.StudentGradeSummaryResponseVO;
import priv.ana.pojo.dto.StartExamRequestDTO;
import priv.ana.pojo.dto.SubmitAnswersRequestDTO;
import priv.ana.pojo.dto.UserAnswersRecordDTO;
import priv.ana.pojo.vo.StartExamResponseVO;

public interface ExamineService {
    StartExamResponseVO startExamine(StartExamRequestDTO startExamRequestDTO, Long userId,  Integer userRole);

    void submitExamine(SubmitAnswersRequestDTO submitAnswersRequestDTO, Long recordId, Long userId, Integer userRole);

    UserAnswersRecordDTO getAnswerRecord(Long examId, Long userId, Integer userRole);

    PaginationResponse<StudentGradeSummaryResponseVO> getGrades(Long page, Long size, Long userId);
}
