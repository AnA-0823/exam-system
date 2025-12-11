package priv.ana.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswersRecordDTO;
import priv.ana.pojo.vo.StudentGradeSummaryResponseVO;

@FeignClient(name = "exam-service", path = "/exams")
public interface ExamClient {

    /**
     * 获取试卷作答记录
     * @param recordId
     * @return
     */
    @GetMapping("/answersRecord/{examId}")
    public Response<UserAnswersRecordDTO> getAnswersRecord(@PathVariable Long recordId);

    /**
     * 获取学生成绩
     * @param page
     * @param size
     * @param studentId
     * @return
     */
    @GetMapping("/grades/{studentId}")
    public Response<PaginationResponse<StudentGradeSummaryResponseVO>> getGrades(@PathVariable Long page, @PathVariable Long size, @PathVariable Long studentId);
}
