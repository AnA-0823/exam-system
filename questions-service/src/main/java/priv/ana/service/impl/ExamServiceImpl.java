package priv.ana.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.ana.common.PaginationResponse;
import priv.ana.mapper.ExamMapper;
import priv.ana.mapper.QuestionMapper;
import priv.ana.pojo.dto.ExamCreateRequestDTO;
import priv.ana.pojo.dto.ExamUpdateRequestDTO;
import priv.ana.pojo.dto.QuestionCreationRequestDTO;
import priv.ana.pojo.entity.Exam;
import priv.ana.pojo.entity.Question;
import priv.ana.pojo.vo.ExamDetailResponseVO;
import priv.ana.pojo.vo.ExamSummaryResponseVO;
import priv.ana.pojo.vo.QuestionDetailResponseVO;
import priv.ana.pojo.vo.QuestionResponseVO;
import priv.ana.service.ExamService;
import priv.ana.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    ExamMapper examMapper;

    @Override
    @Transactional
    public ExamDetailResponseVO createExam(ExamCreateRequestDTO request) {
        // 创建试卷
        Exam exam = new Exam();
        BeanUtils.copyProperties(request, exam);
        exam.setEndTime(request.getStartTime().plusMinutes(request.getDuration()));
        examMapper.insert(exam);
        List<QuestionDetailResponseVO> questionDetailResponseVOS = new ArrayList<>();
        // 创建题目
        request.getQuestions().forEach(questionInExamDTO -> {
            QuestionCreationRequestDTO questionCreationRequestDTO = new QuestionCreationRequestDTO();
            BeanUtils.copyProperties(questionInExamDTO, questionCreationRequestDTO);
            questionCreationRequestDTO.setExamId(exam.getId());
            QuestionResponseVO questionResponseVO = questionService.createQuestion(questionCreationRequestDTO);
            // 将题目详情响应VO转换为学生题目详情响应VO
            QuestionDetailResponseVO questionDetailResponseVO = new QuestionDetailResponseVO();
            BeanUtils.copyProperties(questionResponseVO, questionDetailResponseVO);
            questionDetailResponseVOS.add(questionDetailResponseVO);
        });;
        // 将试卷详情响应VO转换为学生试卷详情响应VO
        ExamDetailResponseVO examDetailResponseVO = new ExamDetailResponseVO();
        BeanUtils.copyProperties(exam, examDetailResponseVO);
        examDetailResponseVO.setQuestions(questionDetailResponseVOS);
        return examDetailResponseVO;
    }

    @Override
    @Transactional
    public void updateExam(Long examId, ExamUpdateRequestDTO request) {
        // 更新题目
        List<QuestionResponseVO> questions = questionService.getQuestions(1L, 10086L, examId).getRecords();
        questionService.deleteQuestions(questions);
        request.getQuestions().forEach(questionInExamDTO -> {
            QuestionCreationRequestDTO questionCreationRequestDTO = new QuestionCreationRequestDTO();
            BeanUtils.copyProperties(questionInExamDTO, questionCreationRequestDTO);
            questionCreationRequestDTO.setExamId(examId);
            questionService.createQuestion(questionCreationRequestDTO);
        });
        // 更新试卷信息
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Exam::getId, examId);

        Exam exam = new Exam();
        BeanUtils.copyProperties(request, exam);
        examMapper.update(exam, wrapper);
    }

    @Override
    @Transactional
    public void deleteExam(Long examId) {
        questionService.deleteQuestionsByExamId(examId);
        int i = examMapper.deleteById(examId);
        if(i==0){
           throw new IllegalArgumentException("没有找到该试卷");
        }
    }

    @Override
    public void publishExam(Long examId) {

    }

    @Override
    public ExamDetailResponseVO getExamDetail(Long examId) {
        // 获取题目
        PaginationResponse<QuestionResponseVO> questionPage = questionService.getQuestions(1L, 10086L, examId);

        // 将题目详情响应VO转换为学生题目详情响应VO
        List<QuestionResponseVO> questionResponseVOs = questionPage.getRecords();
        List<QuestionDetailResponseVO> questionDetailResponseVOS = new ArrayList<>();
        questionResponseVOs.forEach(questionResponseVO -> {
            QuestionDetailResponseVO questionDetailResponseVO = new QuestionDetailResponseVO();
            BeanUtils.copyProperties(questionResponseVO, questionDetailResponseVO);
            questionDetailResponseVOS.add(questionDetailResponseVO);
        });

        Exam exam = examMapper.selectById(examId);
        ExamDetailResponseVO examDetailResponseVO = new ExamDetailResponseVO();
        BeanUtils.copyProperties(exam, examDetailResponseVO);
        examDetailResponseVO.setQuestions(questionDetailResponseVOS);
        return examDetailResponseVO;
    }

    @Override
    public PaginationResponse<ExamSummaryResponseVO> getExams(Long pageNum, Long pageSize) {
        Page<Exam> page = Page.of(pageNum, pageSize);
        Page<Exam> examPage = examMapper.selectPage(page, null);
        // 将试卷详情转换成试卷摘要
        List<ExamSummaryResponseVO> examSummaryResponseVOS = examPage.getRecords().stream().map(exam -> {
            ExamSummaryResponseVO examSummaryResponseVO = new ExamSummaryResponseVO();
            BeanUtils.copyProperties(exam, examSummaryResponseVO);
            return examSummaryResponseVO;
        }).toList();
        // 封装分页响应
        PaginationResponse<ExamSummaryResponseVO> paginationResponse = new PaginationResponse<>();
        BeanUtils.copyProperties(examPage, paginationResponse);
        paginationResponse.setPages(examPage.getPages());
        paginationResponse.setRecords(examSummaryResponseVOS);
        return paginationResponse;
    }
}
