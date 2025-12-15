package priv.ana.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.dtos.examServiceDTO.ExamRecordResponseDTO;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswersRecordDTO;
import priv.ana.pojo.vo.StudentGradeSummaryResponseVO;

@FeignClient(name = "exam-service", path = "/examine")
public interface ExamClient {

    /**
     * 获取学生成绩
     * @param page
     * @param size
     * @param studentId
     * @return
     */
    @GetMapping("/grades")
    public Response<PaginationResponse<StudentGradeSummaryResponseVO>> getGrades(@RequestParam Long page,@RequestParam Long size,@RequestHeader("User-Id") String userId);

    /**
     * 查询题目作答列表
     * @param examId
     * @param userId
     * @param userRole
     * @return
     */
    @GetMapping("/getAnswersRecord/{examId}")
    public Response<UserAnswersRecordDTO> getAnswersRecord(@PathVariable Long examId,@RequestHeader("User-Id") String userId,@RequestHeader("User-Role") String userRole);

    /**
     * 获取考试记录
     * @param recordId
     * @return
     */
    @GetMapping("/getExamRecord/{recordId}")
    public Response<ExamRecordResponseDTO> getExamRecord(@PathVariable Long recordId);
}
