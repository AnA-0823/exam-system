package priv.ana.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.ana.client.QuestionsClient;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.vos.quesitonsVO.ExamDetailResponseVO;
import priv.ana.core.web.domain.vos.summaryVO.StudentGradeSummaryResponseVO;
import priv.ana.mapper.AnswerRecordMapper;
import priv.ana.mapper.ExamRecordMapper;
import priv.ana.pojo.dto.StartExamRequestDTO;
import priv.ana.pojo.dto.SubmitAnswersRequestDTO;
import priv.ana.pojo.dto.UserAnswerRecordDTO;
import priv.ana.pojo.dto.UserAnswersRecordDTO;
import priv.ana.pojo.entity.AnswerRecord;
import priv.ana.pojo.entity.ExamRecord;
import priv.ana.pojo.vo.StartExamResponseVO;
import priv.ana.service.ExamineService;

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

    @Override
    public StartExamResponseVO startExamine(StartExamRequestDTO startExamRequestDTO, Long userId,  Integer userRole) {
        //todo: 完善继续考试逻辑
        ExamRecord examRecord = new ExamRecord();
        examRecord.setUserId(userId);
        examRecord.setExamId(startExamRequestDTO.getExamId());
        // 尝试从记录数据库中直接获取当前考试名称（想优化查询流程，结果发现后面还是要查询exam表，废弃）
//        LambdaQueryWrapper<ExamRecord> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(ExamRecord::getExamId, startExamRequestDTO.getExamId());
//        ExamRecord examRecord1 = examineMapper.selectOne(wrapper);
//        if(examRecord1 != null)
//            examRecord.setExamTitle(examRecord1.getExamTitle());
//        else{
//            // 记录中没有，只能查试卷表
//            ExamDetailResponseVO examDetailResponseVO = questionsClient.getExam(startExamRequestDTO.getExamId()).getData();
//            if(examDetailResponseVO == null){
//                throw new RuntimeException("没有找到该考试");
//            }
//            examRecord.setExamTitle(examDetailResponseVO.getTitle());
//        }
        // todo: 所有服务间的调用都应该检查是否成功
        ExamDetailResponseVO examDetailResponseVO = questionsClient.getExam(startExamRequestDTO.getExamId()).getData();
        if(examDetailResponseVO == null){
            throw new RuntimeException("没有找到该考试");
        }
        examRecord.setExamTitle(examDetailResponseVO.getTitle());
        examRecordMapper.insert(examRecord);
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
    public void submitExamine(SubmitAnswersRequestDTO submitAnswersRequestDTO, Long recordId, Long userId, Integer userRole) {
        //两种情况：1.定时保存 2.交卷
        //共性：作答结果保存，根据recordId，questionId将作答情况存入exam_record_answers表
        submitAnswersRequestDTO.getAnswers().forEach(userAnswerDTO -> {
            //单题保存流程：
            //1. 根据questionId，recordId查询exam_record_answers表，判断是否已存在,存在时更新，否则新增
            LambdaQueryWrapper<AnswerRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AnswerRecord::getQuestionId, userAnswerDTO.getQuestionId());
            wrapper.eq(AnswerRecord::getRecordId, recordId);
            AnswerRecord answerRecord1 = answerRecordMapper.selectOne(wrapper);
            if(answerRecord1 != null){
                // 已存在，更新作答
                answerRecord1.setUserAnswer(userAnswerDTO.getUserAnswer());
                answerRecordMapper.updateById(answerRecord1);
            }else{
                // 不存在，新增作答
                AnswerRecord answerRecord = new AnswerRecord();
                answerRecord.setQuestionId(userAnswerDTO.getQuestionId());
                answerRecord.setRecordId(recordId);
                answerRecord.setUserAnswer(userAnswerDTO.getUserAnswer());
                answerRecordMapper.insert(answerRecord);
            }
        });
        //情况1：定时保存：无其他操作，直接返回
        if(!submitAnswersRequestDTO.getIsFinalSubmit()){
            return;
        }
        //情况2：更新exam_record表的submit_time
        //分离评分功能，仅存储作答结果 todo:交卷：根据questionId查询question表，判断是否正确并统计得分；计算最终得分，
        ExamRecord examRecord = new ExamRecord();
        examRecord.setId(recordId);
        examRecord.setSubmitTime(LocalDateTime.now());
        examRecordMapper.updateById(examRecord);
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
        userAnswersRecordDTO.setAnswers(answerRecordDTOS);
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
}
