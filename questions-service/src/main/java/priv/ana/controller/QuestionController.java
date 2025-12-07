package priv.ana.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.pojo.dto.QuestionUpdateRequestDTO;
import priv.ana.pojo.vo.QuestionResponseVO;
import priv.ana.service.QuestionService;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    /**
     * 修改题目信息
     * @param request
     * @param questionId
     * @return
     */
    @PutMapping("/{questionId}")
    public Response<Void> updateQuestion(@Valid @RequestBody QuestionUpdateRequestDTO request, @PathVariable Long questionId){
        questionService.updateQuestion(request,questionId);
        return Response.success();
    }

    /**
     * 删除题目
     * @param questionId
     * @return
     */
    @DeleteMapping("/{questionId}")
    public Response<Void> deleteQuestion(@PathVariable Long questionId){
        questionService.deleteQuestion(questionId);
        return Response.success();
    }

    /**
     * 查询题目详情
     * @param questionId
     * @return
     */
    @GetMapping("/{questionId}")
    public Response<QuestionResponseVO> getQuestion(@PathVariable Long questionId){
        QuestionResponseVO question = questionService.getQuestion(questionId);
        return Response.success(question);
    }

    /**
     * 查询题目列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public Response<PaginationResponse<QuestionResponseVO>> getQuestions(@RequestParam(required = false) Long page,
                                                                         @RequestParam(required = false) Long size,
                                                                         @RequestParam(required = false) Long examId){
        PaginationResponse<QuestionResponseVO> questions = questionService.getQuestions(page,size,examId);
        return Response.success(questions);
    }
}
