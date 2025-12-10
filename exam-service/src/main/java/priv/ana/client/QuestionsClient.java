package priv.ana.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.vos.quesitonsVO.ExamDetailResponseVO;

@FeignClient(name = "questions-service",path = "/exams")
public interface QuestionsClient {
    /**
     * 查询试卷详情
     * @param examId
     * @return
     */
    @GetMapping("/{examId}")
    public Response<ExamDetailResponseVO> getExam(@PathVariable Long examId);
}
