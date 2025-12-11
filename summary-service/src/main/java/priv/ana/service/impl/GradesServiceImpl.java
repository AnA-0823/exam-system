package priv.ana.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.ana.client.ExamClient;
import priv.ana.client.QuestionsClient;
import priv.ana.common.PaginationResponse;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswerRecordDTO;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswersRecordDTO;
import priv.ana.core.web.domain.vos.questionServiceVO.QuestionResponseVO;
import priv.ana.enums.AnswerResult;
import priv.ana.exception.EmptyResultException;
import priv.ana.mapper.GradesMapper;
import priv.ana.pojo.entity.GradeStatistic;
import priv.ana.pojo.vo.GradeStatisticsResponseVO;
import priv.ana.pojo.vo.QuestionAnswerDetailVO;
import priv.ana.pojo.vo.StudentAnswerDetailResponseVO;
import priv.ana.pojo.vo.StudentGradeSummaryResponseVO;
import priv.ana.service.GradesService;

import java.util.List;

@Service
@Slf4j
public class GradesServiceImpl implements GradesService {

    @Autowired
    GradesMapper gradesMapper;

    @Autowired
    QuestionsClient questionsClient;

    @Autowired
    ExamClient examClient;

    @Override
    public GradeStatisticsResponseVO getGradeStatistics(Long examId) {
        //根据examId查询考试记录
        LambdaQueryWrapper<GradeStatistic> warpper = new LambdaQueryWrapper<>();
        warpper.eq(GradeStatistic::getExamId, examId);
        GradeStatistic gradeStatistic = gradesMapper.selectOne(warpper);
        if(gradeStatistic == null){
            log.info("examid:{},未找到考试记录", examId);
            return null;
        }
        GradeStatisticsResponseVO gradeStatisticsResponseVO = new GradeStatisticsResponseVO();
        BeanUtils.copyProperties(gradeStatistic, gradeStatisticsResponseVO);
        return gradeStatisticsResponseVO;
    }

    @Override
    public StudentAnswerDetailResponseVO getRecord(Long recordId) {
        //根据recordId,查询exam-records表，装填StudentAnswerDetailResponseVO
        StudentAnswerDetailResponseVO studentAnswerDetailResponseVO = new StudentAnswerDetailResponseVO();
        GradeStatistic gradeStatistic = gradesMapper.selectById(recordId);
        if(gradeStatistic == null){
           throw new EmptyResultException("未找到考试记录");
        }
        BeanUtils.copyProperties(gradeStatistic, studentAnswerDetailResponseVO);
        //根据examId，查询quesitons表,获取题目信息
        List<QuestionResponseVO> questionResponseVOS = questionsClient.getQuestions(0L, 10086L, gradeStatistic.getExamId()).getData().getRecords();
        //根据recordId,查询exam_record_answers表，获取作答结果
        UserAnswersRecordDTO answersRecord = examClient.getAnswersRecord(recordId).getData();
        //遍历试卷的题目，装填QuesitonAnswerDetailVO(注意字段MaxScore)
        questionResponseVOS.forEach(questionResponseVO -> {
            //根据questionId，获取学生该题的userAnswer与score，装填QuesitonAnswerDetailVO
            QuestionAnswerDetailVO questionAnswerDetailVO = new QuestionAnswerDetailVO();
            questionAnswerDetailVO.setMaxScore(questionResponseVO.getScore());
            BeanUtils.copyProperties(questionResponseVO, questionAnswerDetailVO);
            for (UserAnswerRecordDTO userAnswerRecordDTO : answersRecord.getUserAnswerRecordDTOS()) {
                //遍历作答列表取出questionId匹配的作答,获取userAnswer与score,匹配完成后从作答列表移除这项
                if (questionAnswerDetailVO.getQuestionId().equals(userAnswerRecordDTO.getQuestionId())) {
                    questionAnswerDetailVO.setUserAnswer(userAnswerRecordDTO.getUserAnswer());
                    questionAnswerDetailVO.setScore(userAnswerRecordDTO.getScore());
                    questionAnswerDetailVO.setResult(AnswerResult.getAnswerResult(questionAnswerDetailVO.getScore() > 0));
                    answersRecord.getUserAnswerRecordDTOS().remove(userAnswerRecordDTO);
                    break;
                }
            }
            studentAnswerDetailResponseVO.getAnswers().add(questionAnswerDetailVO);
        });
        //将组装好的QuesitonAnswerDetailVO数组装填回StudentAnswerDetailResponseVO，返回
        return studentAnswerDetailResponseVO;
    }

    @Override
    public PaginationResponse<StudentGradeSummaryResponseVO> getGrades(Long page, Long size, Long studentId) {
        //根据studentId,查询exam-records表，装填StudentGradeSummaryResponseVO
        PaginationResponse<StudentGradeSummaryResponseVO> grades = examClient.getGrades(page, size, studentId).getData();
        return grades;
    }


}
