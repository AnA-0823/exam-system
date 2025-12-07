package priv.ana.service;

import priv.ana.common.PaginationResponse;
import priv.ana.pojo.dto.QuestionCreationRequestDTO;
import priv.ana.pojo.dto.QuestionUpdateRequestDTO;
import priv.ana.pojo.vo.QuestionResponseVO;

import java.util.List;

public interface QuestionService {

    QuestionResponseVO createQuestion(QuestionCreationRequestDTO request);

    void updateQuestion(QuestionUpdateRequestDTO request, Long questionId);

    void deleteQuestion(Long questionId);

    void deleteQuestions(List<QuestionResponseVO> questions);

    QuestionResponseVO getQuestion(Long questionId);

    PaginationResponse<QuestionResponseVO> getQuestions(Long pageNum, Long pageSize,Long examId);

    void deleteQuestionsByExamId(Long examId);
}
