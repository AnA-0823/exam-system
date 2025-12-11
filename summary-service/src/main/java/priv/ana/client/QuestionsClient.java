package priv.ana.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.vos.questionServiceVO.QuestionResponseVO;

@FeignClient(name = "questions-service", path = "/questions")
public interface QuestionsClient {

    /**
     * 查询题目列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public Response<PaginationResponse<QuestionResponseVO>> getQuestions(@RequestParam(required = false) Long page,
                                                                         @RequestParam(required = false) Long size,
                                                                         @RequestParam(required = false) Long examId);
}
