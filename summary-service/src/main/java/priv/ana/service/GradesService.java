package priv.ana.service;

import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.dtos.examServiceDTO.ExamRecordResponseDTO;
import priv.ana.pojo.vo.GradeStatisticsResponseVO;
import priv.ana.pojo.vo.StudentAnswerDetailResponseVO;
import priv.ana.pojo.vo.StudentGradeSummaryResponseVO;

public interface GradesService {
    GradeStatisticsResponseVO getGradeStatistics(Long examId);

    StudentAnswerDetailResponseVO getRecord(Long recordId, String userId, String userRole);

    PaginationResponse<StudentGradeSummaryResponseVO> getGrades(Long page, Long size, String userId);

    void updateGradeStatistics(ExamRecordResponseDTO examRecordResponseDTO);
}
