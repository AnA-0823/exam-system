package priv.ana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.pojo.vo.GradeStatisticsResponseVO;
import priv.ana.pojo.vo.StudentAnswerDetailResponseVO;
import priv.ana.pojo.vo.StudentGradeSummaryResponseVO;
import priv.ana.service.GradesService;


@RestController
@RequestMapping("/grades")
public class GradesController {

    @Autowired
    GradesService gradesService;

    @GetMapping("/statistics/{examId}")
    public Response<GradeStatisticsResponseVO> getGradeStatistics(@PathVariable Long examId) {
        GradeStatisticsResponseVO gradeStatistics = gradesService.getGradeStatistics(examId);
        return Response.success(gradeStatistics);
    }

    @GetMapping("record/{recordId}")
    public Response<StudentAnswerDetailResponseVO> getRecord(@PathVariable Long recordId) {
        StudentAnswerDetailResponseVO record = gradesService.getRecord(recordId);
        return Response.success(record);
    }

    @GetMapping
    public Response<PaginationResponse<StudentGradeSummaryResponseVO>> getGrades(Long page, Long size, @RequestHeader("User-Id") String userId) {
        PaginationResponse<StudentGradeSummaryResponseVO> grades = gradesService.getGrades(page, size, Long.valueOf(userId));
        return Response.success(grades);
    }
}
