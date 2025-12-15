package priv.ana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.dtos.examServiceDTO.ExamRecordResponseDTO;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswersRecordDTO;
import priv.ana.core.web.domain.vos.summaryVO.StudentGradeSummaryResponseVO;
import priv.ana.pojo.dto.StartExamRequestDTO;
import priv.ana.pojo.dto.SubmitAnswersRequestDTO;

import priv.ana.pojo.vo.StartExamResponseVO;
import priv.ana.service.ExamineService;

@RestController
@RequestMapping("/examine")
public class ExamineController {

    @Autowired
    private ExamineService examineService;

    /**
     * 开始考试
     * @param startExamRequestDTO
     * @param userId
     * @param userRole
     * @return
     */
    @PostMapping("/start")
    public Response<StartExamResponseVO> startExamine(@RequestBody StartExamRequestDTO startExamRequestDTO, @RequestHeader("User-Id") String userId,@RequestHeader("User-Role") String userRole) {
        StartExamResponseVO startExamResponseVO = examineService.startExamine(startExamRequestDTO,Long.valueOf(userId) ,Integer.valueOf(userRole));
        return Response.success(startExamResponseVO);
    }

    /**
     * 提交答案
     * @param submitAnswersRequestDTO
     * @param recordId
     * @param userId
     * @param userRole
     * @return
     */
    @PostMapping("/submit/{recordId}")
    public Response<Void> submitExamine(@RequestBody SubmitAnswersRequestDTO submitAnswersRequestDTO, @PathVariable Long recordId,@RequestHeader("User-Id") String userId,@RequestHeader("User-Role") String userRole) {
        examineService.submitExamine(submitAnswersRequestDTO, recordId,Long.valueOf(userId),Integer.valueOf(userRole));
        return Response.success();
    }

    /**
     * 查询题目作答列表
     * @param examId
     * @param userId
     * @param userRole
     * @return
     */
    @GetMapping("/getAnswersRecord/{examId}")
    public Response<UserAnswersRecordDTO> getAnswersRecord(@PathVariable Long examId, @RequestHeader("User-Id") String userId, @RequestHeader("User-Role") String userRole) {
        UserAnswersRecordDTO studentAnswerDetailResponseVO = examineService.getAnswerRecord(examId,Long.valueOf(userId),Integer.valueOf(userRole));
        return Response.success(studentAnswerDetailResponseVO);
    }

    /**
     * 查询学生考试记录列表
     * @param page
     * @param size
     * @param userId
     * @return
     */
    @GetMapping("/grades")
    public Response<PaginationResponse<StudentGradeSummaryResponseVO>> getGrades(@RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "10") Long size,@RequestHeader("User-Id") String userId){
        PaginationResponse<StudentGradeSummaryResponseVO> grades = examineService.getGrades(page, size, Long.valueOf(userId));
        return Response.success(grades);
    }


    /**
     * 获取考试记录
     * @param recordId
     * @return
     */
    @GetMapping("/getExamRecord/{recordId}")
    public Response<ExamRecordResponseDTO> getExamRecord(@PathVariable Long recordId){
    	ExamRecordResponseDTO examRecordResponseDTO = examineService.getExamRecord(recordId);
    	return Response.success(examRecordResponseDTO);
    }
}
