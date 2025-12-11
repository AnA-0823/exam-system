package priv.ana.service;

import priv.ana.common.PaginationResponse;
import priv.ana.pojo.vo.GradeStatisticsResponseVO;
import priv.ana.pojo.vo.StudentAnswerDetailResponseVO;
import priv.ana.pojo.vo.StudentGradeSummaryResponseVO;

public interface GradesService {
    GradeStatisticsResponseVO getGradeStatistics(Long examId);

    StudentAnswerDetailResponseVO getRecord(Long recordId);

    PaginationResponse<StudentGradeSummaryResponseVO> getGrades(Long page, Long size, Long studentId);
}
