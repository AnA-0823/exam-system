package priv.ana.service;

import priv.ana.common.PaginationResponse;
import priv.ana.pojo.dto.ExamCreateRequestDTO;
import priv.ana.pojo.dto.ExamUpdateRequestDTO;
import priv.ana.pojo.vo.ExamDetailResponseVO;
import priv.ana.pojo.vo.ExamSummaryResponseVO;

public interface ExamService {

    ExamDetailResponseVO createExam(ExamCreateRequestDTO request);

    void updateExam(Long examId, ExamUpdateRequestDTO request);

    void deleteExam(Long examId);

    void publishExam(Long examId);

    ExamDetailResponseVO getExamDetail(Long examId);

    PaginationResponse<ExamSummaryResponseVO> getExams(Long page, Long size);
}
