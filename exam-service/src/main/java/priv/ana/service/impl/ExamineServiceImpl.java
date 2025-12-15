package priv.ana.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.ana.client.QuestionsClient;
import priv.ana.client.SummaryClient;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.dtos.examServiceDTO.ExamRecordResponseDTO;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswerRecordDTO;
import priv.ana.core.web.domain.vos.quesitonsVO.ExamDetailResponseVO;
import priv.ana.core.web.domain.vos.questionServiceVO.QuestionResponseVO;
import priv.ana.core.web.domain.vos.summaryVO.StudentGradeSummaryResponseVO;
import priv.ana.mapper.AnswerRecordMapper;
import priv.ana.mapper.ExamRecordMapper;
import priv.ana.pojo.dto.*;
import priv.ana.pojo.entity.AnswerRecord;
import priv.ana.pojo.entity.ExamRecord;
import priv.ana.pojo.vo.StartExamResponseVO;
import priv.ana.service.ExamineService;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswersRecordDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamineServiceImpl implements ExamineService {

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Autowired
    private AnswerRecordMapper answerRecordMapper;

    @Autowired
    private QuestionsClient questionsClient;

    @Autowired
    private SummaryClient summaryClient;

    @Override
    public StartExamResponseVO startExamine(StartExamRequestDTO startExamRequestDTO, Long userId,  Integer userRole) {
        // todo: 加redis
        // todo: 所有服务间的调用都应该检查是否成功
        ExamDetailResponseVO examDetailResponseVO = questionsClient.getExam(startExamRequestDTO.getExamId()).getData();
        if(examDetailResponseVO == null){
            throw new RuntimeException("没有找到该考试");
        }else if(examDetailResponseVO.getStartTime().isAfter(LocalDateTime.now())){
            throw new RuntimeException("该考试尚未开始");
        }else if(examDetailResponseVO.getEndTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("该考试已结束");
        }
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getExamId, startExamRequestDTO.getExamId());
        wrapper.eq(ExamRecord::getUserId, userId);
        // 获取考试记录，判断该考生是否已经开始过
        ExamRecord examRecord = examRecordMapper.selectOne(wrapper);
        if(examRecord == null){
            // 新考试
            examRecord = new ExamRecord();
            examRecord.setUserId(userId);
            examRecord.setExamId(startExamRequestDTO.getExamId());
            examRecord.setExamTitle(examDetailResponseVO.getTitle());
            examRecordMapper.insert(examRecord);
        }
        StartExamResponseVO startExamResponseVO = new StartExamResponseVO();
        startExamResponseVO.setDuration(examDetailResponseVO.getDuration());
        startExamResponseVO.setQuestions(examDetailResponseVO.getQuestions());
        startExamResponseVO.setStartTime(examRecord.getCreatedAt());
        startExamResponseVO.setRecordId(examRecord.getId());
        startExamResponseVO.setExamId(examRecord.getExamId());
        startExamResponseVO.setExamTitle(examRecord.getExamTitle());
        return startExamResponseVO;
    }

    @Override
    @Transactional
    public void submitExamine(SubmitAnswersRequestDTO submitAnswersRequestDTO, Long recordId, Long userId, Integer userRole) {

        ExamRecord examRecord1 = examRecordMapper.selectById(recordId);
        if(examRecord1 == null){
            throw new RuntimeException("没有找到该考试记录");
        }else if(examRecord1.getFinalScore()!=null)
            throw new RuntimeException("您已完成提交，不可重复提交");
        Long examId = examRecord1.getExamId();
        ExamDetailResponseVO exam = questionsClient.getExam(examId).getData();
        if(exam== null){
            throw new RuntimeException("未找到该试卷");
        }
        if(exam.getEndTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("该考试已结束,不能提交");
        }
        //两种情况：1.定时保存 2.交卷
        //共性：作答结果保存，根据recordId，questionId将作答情况存入answer_records表
        double finalScore = 0;
        for (int i =0; i< submitAnswersRequestDTO.getAnswers().size(); i++) {//单题保存流程：
            UserAnswerDTO userAnswerDTO = submitAnswersRequestDTO.getAnswers().get(i);
            //1. 根据questionId，recordId查询exam_record_answers表，判断是否已存在,存在时更新，否则新增
            LambdaQueryWrapper<AnswerRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AnswerRecord::getQuestionId, userAnswerDTO.getQuestionId());
            wrapper.eq(AnswerRecord::getRecordId, recordId);
            AnswerRecord answerRecord = answerRecordMapper.selectOne(wrapper);
            if (answerRecord != null) {
                // 已存在，更新作答
                answerRecord.setUserAnswer(userAnswerDTO.getUserAnswer());
                if (submitAnswersRequestDTO.getIsFinalSubmit()) {
                    score(answerRecord);
                    finalScore += answerRecord.getScore();
                }
                answerRecordMapper.updateById(answerRecord);
            } else {
                // 不存在，新增作答
                answerRecord = new AnswerRecord();
                answerRecord.setQuestionId(userAnswerDTO.getQuestionId());
                answerRecord.setRecordId(recordId);
                answerRecord.setUserAnswer(userAnswerDTO.getUserAnswer());
                if (submitAnswersRequestDTO.getIsFinalSubmit()) {
                    score(answerRecord);
                    finalScore += answerRecord.getScore();
                }
                answerRecordMapper.insert(answerRecord);
            }
        }
        //情况1：定时保存：无其他操作，直接返回
        if(!submitAnswersRequestDTO.getIsFinalSubmit()){
            return;
        }
        //情况2：更新exam_record表的submit_time
        //分离评分功能，仅存储作答结果 todo:交卷：根据questionId查询question表，判断是否正确并统计得分；计算最终得分
        //todo:考虑成绩汇总的更新时机：当有新的试卷提交记录时，更新grade_statistics表的average_score,highest_score,num_participants，score_distribution,update_at字段，
        ExamRecord examRecord = new ExamRecord();
        examRecord.setId(recordId);
        examRecord.setSubmitTime(LocalDateTime.now());
        score(examRecord, finalScore);
        examRecordMapper.updateById(examRecord);
        //创建成绩统计
        examRecord = examRecordMapper.selectOne(new LambdaQueryWrapper<ExamRecord>().eq(ExamRecord::getId, recordId));
        ExamRecordResponseDTO examRecordResponseDTO = new ExamRecordResponseDTO();
        examRecordResponseDTO.setExamId(examRecord.getExamId());
        examRecordResponseDTO.setExamTitle(examRecord.getExamTitle());
        examRecordResponseDTO.setFinalScore(examRecord.getFinalScore());
        summaryClient.updateGradeStatistics(examRecordResponseDTO);
    }


    /**
     * 评分
     * @param answerRecord
     * @return
     */
    private void score(AnswerRecord answerRecord) {
        Response<QuestionResponseVO> question = questionsClient.getQuestion(answerRecord.getQuestionId());
        if(question.getData() == null){
            throw new RuntimeException("没有找到该题");
        }
        QuestionResponseVO questionDetail = question.getData();
        //判断作答结果
        if(questionDetail.getCorrectAnswer().equals(answerRecord.getUserAnswer())){
            //正确
            answerRecord.setScore(questionDetail.getScore());
        }else{
            //错误
            answerRecord.setScore(0.0);
        }
    }

    //评分
    private void score(ExamRecord examRecord,double finalScore) {
        //总分
        examRecord.setFinalScore(finalScore);
    }

    @Override
    public UserAnswersRecordDTO getAnswerRecord(Long examId, Long userId, Integer userRole) {
        //根据学生id，考试id查询exam_record_answers表，获取该试卷中所有题目的作答情况
        //1. 根据examId，userId查询exam_record表，获取recordId
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getExamId, examId);
        wrapper.eq(ExamRecord::getUserId, userId);
        ExamRecord examRecord = examRecordMapper.selectOne(wrapper);
        if(examRecord == null){
          throw new RuntimeException("没有找到该考试");
        }
        // 2. 根据recordId，questionId查询exam_record_answers表，获取该试卷中所有题目的作答情况
        LambdaQueryWrapper<AnswerRecord> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(AnswerRecord::getRecordId, examRecord.getId());
        List<AnswerRecord> answerRecords = answerRecordMapper.selectList(wrapper1);
        //3. 转换为DTO
        List<UserAnswerRecordDTO> answerRecordDTOS = answerRecords.stream().map(answerRecord -> {
            UserAnswerRecordDTO userAnswerRecordDTO = new UserAnswerRecordDTO();
            BeanUtils.copyProperties(answerRecord, userAnswerRecordDTO);
            return userAnswerRecordDTO;
        }).collect(Collectors.toList());
        UserAnswersRecordDTO userAnswersRecordDTO = new UserAnswersRecordDTO();
        userAnswersRecordDTO.setUserAnswerRecordDTOS(answerRecordDTOS);
        return userAnswersRecordDTO;
    }

    @Override
    public PaginationResponse<StudentGradeSummaryResponseVO> getGrades(Long pageNum, Long pageSize, Long userId) {
        //1. 根据studentId，examId查询exam_record表，获取该学生的所有考试记录
        Page<ExamRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamRecord::getUserId, userId);
        Page<ExamRecord> examRecordPage = examRecordMapper.selectPage(page, wrapper);
        //2.转换为VO
        List<StudentGradeSummaryResponseVO> studentGradeSummaryResponseVOS = examRecordPage.getRecords().stream().map(examRecord -> {
            StudentGradeSummaryResponseVO studentGradeSummaryResponseVO = new StudentGradeSummaryResponseVO();
            BeanUtils.copyProperties(examRecord, studentGradeSummaryResponseVO);
            studentGradeSummaryResponseVO.setRecordId(examRecord.getId());
            return studentGradeSummaryResponseVO;
        }).collect(Collectors.toList());
        //3. 封装成分页响应
        PaginationResponse<StudentGradeSummaryResponseVO> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCurrent(pageNum);
        paginationResponse.setSize(pageSize);
        paginationResponse.setTotal(examRecordPage.getTotal());
        paginationResponse.setPages(examRecordPage.getPages());
        paginationResponse.setRecords(studentGradeSummaryResponseVOS);
        return paginationResponse;
    }

    @Override
    public ExamRecordResponseDTO getExamRecord(Long recordId) {
        ExamRecord examRecord = examRecordMapper.selectById(recordId);
        ExamRecordResponseDTO examRecordResponseDTO = new ExamRecordResponseDTO();
        BeanUtils.copyProperties(examRecord, examRecordResponseDTO);
        return examRecordResponseDTO;
    }
}
