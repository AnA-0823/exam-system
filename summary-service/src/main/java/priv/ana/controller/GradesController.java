package priv.ana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.dtos.examServiceDTO.ExamRecordResponseDTO;
import priv.ana.core.web.domain.dtos.summaryServiceDTO.GradeStatisticsCreateRequestDTO;
import priv.ana.pojo.vo.GradeStatisticsResponseVO;
import priv.ana.pojo.vo.StudentAnswerDetailResponseVO;
import priv.ana.pojo.vo.StudentGradeSummaryResponseVO;
import priv.ana.service.GradesService;


@RestController
@RequestMapping("/grades")
public class GradesController {

    @Autowired
    GradesService gradesService;

    /**
     * 查询成绩统计
     * @param examId
     * @return
     */
    @GetMapping("/statistics/{examId}")
    public Response<GradeStatisticsResponseVO> getGradeStatistics(@PathVariable Long examId) {
        GradeStatisticsResponseVO gradeStatistics = gradesService.getGradeStatistics(examId);
        return Response.success(gradeStatistics);
    }

    /**
     * 查询答题详情
     * @param recordId
     * @return
     */
    @GetMapping("record/{recordId}")
    public Response<StudentAnswerDetailResponseVO> getRecord(@PathVariable Long recordId,@RequestHeader("User-Id") String userId,@RequestHeader("User-Role") String userRole) {
        StudentAnswerDetailResponseVO record = gradesService.getRecord(recordId, userId, userRole);
        return Response.success(record);
    }

    /**
     * 查询学生个人成绩
     * @param page
     * @param size
     * @param userId
     * @return
     */
    @GetMapping
    public Response<PaginationResponse<StudentGradeSummaryResponseVO>> getGrades(Long page, Long size, @RequestHeader("User-Id") String userId) {
        PaginationResponse<StudentGradeSummaryResponseVO> grades = gradesService.getGrades(page, size, userId);
        return Response.success(grades);
    }

    /**
     * 更新成绩统计
     * @param examRecordResponseDTO
     * @return
     */
    @PutMapping
    public Response<Void> updateGradeStatistics(@RequestBody ExamRecordResponseDTO examRecordResponseDTO) {
        gradesService.updateGradeStatistics(examRecordResponseDTO);
        return Response.success();
    }
}
