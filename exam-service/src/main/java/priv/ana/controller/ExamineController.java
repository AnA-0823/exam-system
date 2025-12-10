package priv.ana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.vos.summaryVO.StudentGradeSummaryResponseVO;
import priv.ana.pojo.dto.StartExamRequestDTO;
import priv.ana.pojo.dto.SubmitAnswersRequestDTO;
import priv.ana.pojo.dto.UserAnswersRecordDTO;
import priv.ana.pojo.vo.StartExamResponseVO;
import priv.ana.service.ExamineService;

@RestController
@RequestMapping("/examine")
public class ExamineController {

    @Autowired
    private ExamineService examineService;

    @PostMapping("/start")
    public Response<StartExamResponseVO> startExamine(@RequestBody StartExamRequestDTO startExamRequestDTO, @RequestHeader("User-Id") String userId,@RequestHeader("User-Role") String userRole) {
        StartExamResponseVO startExamResponseVO = examineService.startExamine(startExamRequestDTO,Long.valueOf(userId) ,Integer.valueOf(userRole));
        return Response.success(startExamResponseVO);
    }

    @PostMapping("/submit/{recordId}")
    public Response<Void> submitExamine(@RequestBody SubmitAnswersRequestDTO submitAnswersRequestDTO, @PathVariable Long recordId,@RequestHeader("User-Id") String userId,@RequestHeader("User-Role") String userRole) {
        examineService.submitExamine(submitAnswersRequestDTO, recordId,Long.valueOf(userId),Integer.valueOf(userRole));
        return Response.success();
    }

    @GetMapping("/getRecord/{examId}")
    public Response<UserAnswersRecordDTO> getRecord(@PathVariable Long examId,@RequestHeader("User-Id") String userId,@RequestHeader("User-Role") String userRole) {
        UserAnswersRecordDTO studentAnswerDetailResponseVO = examineService.getAnswerRecord(examId,Long.valueOf(userId),Integer.valueOf(userRole));
        return Response.success(studentAnswerDetailResponseVO);
    }

    @GetMapping("/grades")
    public Response<PaginationResponse<StudentGradeSummaryResponseVO>> getGrades(Long page, Long size,@RequestHeader("User-Id") String userId){
        PaginationResponse<StudentGradeSummaryResponseVO> grades = examineService.getGrades(page, size, Long.valueOf(userId));
        return Response.success(grades);
    }
}
