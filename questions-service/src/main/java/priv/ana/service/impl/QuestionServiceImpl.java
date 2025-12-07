package priv.ana.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.ana.common.PaginationResponse;
import priv.ana.mapper.QuestionMapper;
import priv.ana.pojo.dto.QuestionCreationRequestDTO;
import priv.ana.pojo.dto.QuestionOptionDTO;
import priv.ana.pojo.dto.QuestionUpdateRequestDTO;
import priv.ana.pojo.entity.Question;
import priv.ana.pojo.vo.QuestionResponseVO;
import priv.ana.service.QuestionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Override
    @Transactional
    public QuestionResponseVO createQuestion(QuestionCreationRequestDTO questionOptionDTO) {
        // 检查key是否重复
        checkDuplicateKey(questionOptionDTO.getOptions());

        Question question = new Question();
        BeanUtils.copyProperties(questionOptionDTO, question);
        questionMapper.insert(question);

        QuestionResponseVO questionResponseVO = new QuestionResponseVO();
        BeanUtils.copyProperties(question, questionResponseVO);
        return questionResponseVO;
    }

    @Override
    @Transactional
    public void updateQuestion(QuestionUpdateRequestDTO questionUpdateRequestDTO, Long questionId) {
        // 检查key是否重复
        checkDuplicateKey(questionUpdateRequestDTO.getOptions());

        LambdaUpdateWrapper<Question> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Question::getExamId, questionUpdateRequestDTO.getExamId());

        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequestDTO, question);

        questionMapper.update(question, wrapper);
    }

    @Override
    public void deleteQuestion(Long questionId) {
        questionMapper.deleteById(questionId);
    }

    @Override
    public void deleteQuestions(List<QuestionResponseVO> questions) {
        questionMapper.deleteBatchIds(questions);
    }

    @Override
    public QuestionResponseVO getQuestion(Long questionId) {
        Question question = questionMapper.selectById(questionId);

        QuestionResponseVO questionResponseVO = new QuestionResponseVO();
        BeanUtils.copyProperties(question, questionResponseVO);
        return questionResponseVO;
    }

    @Override
    public PaginationResponse<QuestionResponseVO> getQuestions(Long pageNum, Long pageSize,Long examId) {
        Page<Question> page = Page.of(pageNum, pageSize);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        if(examId != null){
            wrapper.eq(Question::getExamId, examId);
        }
        Page<Question> questionPage = questionMapper.selectPage(page, wrapper);
        List<QuestionResponseVO> questionResponseVOS =questionPage.getRecords().stream().map(question -> {
            QuestionResponseVO questionResponseVO = new QuestionResponseVO();
            BeanUtils.copyProperties(question, questionResponseVO);
            return questionResponseVO;
        }).toList();

        PaginationResponse<QuestionResponseVO> questionResponseVOPaginationResponse = new PaginationResponse<>();
        BeanUtils.copyProperties(questionPage, questionResponseVOPaginationResponse);
        // 设置返回的题目列表
        questionResponseVOPaginationResponse.setRecords(questionResponseVOS);
        return questionResponseVOPaginationResponse;
    }

    @Override
    @Transactional
    public void deleteQuestionsByExamId(Long examId) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getExamId, examId);
        questionMapper.delete(wrapper);
    }

    private void checkDuplicateKey(List<QuestionOptionDTO> options) {
        boolean hasDuplicateKey = options.stream()
                .collect(Collectors.groupingBy(QuestionOptionDTO::getKey, Collectors.counting()))
                .values().stream().anyMatch(cnt -> cnt > 1);
        if (hasDuplicateKey) throw new IllegalArgumentException("选项重复");
    }
}
