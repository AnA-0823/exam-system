package priv.ana.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.pojo.dto.ExamCreateRequestDTO;
import priv.ana.pojo.dto.ExamUpdateRequestDTO;
import priv.ana.pojo.vo.ExamDetailResponseVO;
import priv.ana.pojo.vo.ExamSummaryResponseVO;
import priv.ana.service.ExamService;

@RestController
@RequestMapping("/exams")
@Slf4j
public class ExamController {

    @Autowired
    ExamService examService;

    /**
     * 创建试卷
     * @param request
     * @return
     */
    @PostMapping
    public Response<ExamDetailResponseVO> createExam(@RequestBody ExamCreateRequestDTO request) {
        ExamDetailResponseVO exam = examService.createExam(request);
        return Response.success(exam);
    }

    /**
     * 修改试卷信息
     * @param examId
     * @param request
     * @return
     */
    @PutMapping("/{examId}")
    public Response<Void> updateExam(@PathVariable Long examId, @RequestBody ExamUpdateRequestDTO request) {
        examService.updateExam(examId, request);
        return Response.success();
    }

    /**
     * 删除试卷
     * @param examId
     * @return
     */
    @DeleteMapping("/{examId}")
    public Response<Void> deleteExam(@PathVariable Long examId) {
        examService.deleteExam(examId);
        return Response.success();
    }

    /**
     * 发布试卷
     * @param examId
     * @return
     */
    @PutMapping("/{examId}/publish")
    public Response<Void> publishExam(@PathVariable Long examId) {
        examService.publishExam(examId);
        return Response.success();
    }

    /**
     * 查询试卷详情
     * @param examId
     * @return
     */
    @GetMapping("/{examId}")
    public Response<ExamDetailResponseVO> getExam(@PathVariable Long examId) {
        ExamDetailResponseVO exam = examService.getExamDetail(examId);
        return Response.success(exam);
    }

    /**
     * 查询试卷列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public Response<PaginationResponse<ExamSummaryResponseVO>> getExams(
            @RequestParam(required = false) Long page,
            @RequestParam(required = false) Long size
    ) {
        PaginationResponse<ExamSummaryResponseVO> exams = examService.getExams(page, size);
        return Response.success(exams);
    }
}
