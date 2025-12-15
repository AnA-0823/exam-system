package priv.ana.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.vos.quesitonsVO.ExamDetailResponseVO;
import priv.ana.core.web.domain.vos.questionServiceVO.QuestionResponseVO;

@FeignClient(name = "questions-service")
public interface QuestionsClient {
    /**
     * 查询试卷详情
     * @param examId
     * @return
     */
    @GetMapping("/exams/{examId}")
    public Response<ExamDetailResponseVO> getExam(@PathVariable Long examId);

    /**
     * 查询题目详情
     * @param questionId
     * @return
     */
    @GetMapping("/questions/{questionId}")
    public Response<QuestionResponseVO> getQuestion(@PathVariable Long questionId);
}
