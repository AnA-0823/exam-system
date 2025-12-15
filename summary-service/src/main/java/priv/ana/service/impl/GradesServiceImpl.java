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
import priv.ana.core.web.domain.dtos.examServiceDTO.ExamRecordResponseDTO;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswerRecordDTO;
import priv.ana.core.web.domain.dtos.examServiceDTO.UserAnswersRecordDTO;
import priv.ana.core.web.domain.dtos.summaryServiceDTO.ScoreDistributionEntryDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public StudentAnswerDetailResponseVO getRecord(Long recordId, String userId, String userRole) {
        //  todo:这个接口最好丢到exam-service中，数据来源全在exam-service中
        //根据recordId,查询exam-records表，装填StudentAnswerDetailResponseVO
        StudentAnswerDetailResponseVO studentAnswerDetailResponseVO = new StudentAnswerDetailResponseVO();
        studentAnswerDetailResponseVO.setRecordId(recordId);
        studentAnswerDetailResponseVO.setAnswers(new ArrayList<>());
        ExamRecordResponseDTO gradeStatistic = examClient.getExamRecord(recordId).getData();
        if(gradeStatistic == null){
           throw new EmptyResultException("未找到考试记录");
        }
        BeanUtils.copyProperties(gradeStatistic, studentAnswerDetailResponseVO);
        //根据examId，查询quesitons表,获取题目信息
        List<QuestionResponseVO> questionResponseVOS = questionsClient.getQuestions(0L, 10086L, gradeStatistic.getExamId()).getData().getRecords();
        //根据recordId,查询exam_record_answers表，获取作答结果
        UserAnswersRecordDTO answersRecord = examClient.getAnswersRecord(recordId, userId, userRole).getData();
        if(answersRecord == null)
            throw new EmptyResultException("获取答题记录失败");
        //遍历试卷的题目，装填QuesitonAnswerDetailVO(注意字段MaxScore)
        questionResponseVOS.forEach(questionResponseVO -> {
            //根据questionId，获取学生该题的userAnswer与score，装填QuesitonAnswerDetailVO
            QuestionAnswerDetailVO questionAnswerDetailVO = new QuestionAnswerDetailVO();
            questionAnswerDetailVO.setMaxScore(questionResponseVO.getScore());
            BeanUtils.copyProperties(questionResponseVO, questionAnswerDetailVO);
            questionAnswerDetailVO.setQuestionId(questionResponseVO.getId());
            //遍历作答列表取出questionId匹配的作答,获取userAnswer与score,匹配完成后从作答列表移除这项
            answersRecord.getUserAnswerRecordDTOS()
                    .stream()
                    .filter(d -> d.getQuestionId().equals(questionAnswerDetailVO.getQuestionId()))
                    .findFirst()
                    .ifPresent(d -> {
                        log.info("匹配结果：",d);
                        BeanUtils.copyProperties(d, questionAnswerDetailVO);
                        questionAnswerDetailVO.setResult(AnswerResult.getAnswerResult(d.getScore() > 0));
                        answersRecord.getUserAnswerRecordDTOS().remove(d);
                    });
            studentAnswerDetailResponseVO.getAnswers().add(questionAnswerDetailVO);
        });
        //将组装好的QuesitonAnswerDetailVO数组装填回StudentAnswerDetailResponseVO，返回
        return studentAnswerDetailResponseVO;
    }

    @Override
    public PaginationResponse<StudentGradeSummaryResponseVO> getGrades(Long page, Long size, String userId) {
        //根据studentId,查询exam-records表，装填StudentGradeSummaryResponseVO
        PaginationResponse<StudentGradeSummaryResponseVO> grades = examClient.getGrades(page, size,userId).getData();
        return grades;
    }

    @Override
    public void updateGradeStatistics(ExamRecordResponseDTO examRecordResponseDTO) {
        //根据examId,查询grade-statistics表
        GradeStatistic gradeStatistic = gradesMapper.selectOne(new LambdaQueryWrapper<GradeStatistic>().eq(GradeStatistic::getExamId, examRecordResponseDTO.getExamId()));
        if (gradeStatistic == null) {
            //数据库不存在该考试的统计记录，此为首个提交
            // 创建考试记录
            gradeStatistic = new GradeStatistic();
            gradeStatistic.setExamId(examRecordResponseDTO.getExamId());
            gradeStatistic.setExamTitle(examRecordResponseDTO.getExamTitle());
            gradeStatistic.setNumParticipants(0);
            gradeStatistic.setAverageScore(0.0);
            gradeStatistic.setHighestScore(0.0);
            // 得分区间 0-59 60-69 70-79 80-89 90-100
            List<ScoreDistributionEntryDTO> scoreDistribution = new ArrayList<>();
            scoreDistribution.add(new ScoreDistributionEntryDTO(0, 0, 59));
            scoreDistribution.add(new ScoreDistributionEntryDTO(0, 60, 69));
            scoreDistribution.add(new ScoreDistributionEntryDTO(0, 70, 79));
            scoreDistribution.add(new ScoreDistributionEntryDTO(0, 80, 89));
            scoreDistribution.add(new ScoreDistributionEntryDTO(0, 90, 100));
            gradeStatistic.setScoreDistribution(scoreDistribution);
        }
        //更新考试记录
        // 参与人数
        gradeStatistic.setNumParticipants(gradeStatistic.getNumParticipants() + 1);
        // 平均分 = (平均分 * (原参与人数) + 新记录得分) / 参与人数
        double avg = (gradeStatistic.getAverageScore() * (gradeStatistic.getNumParticipants() - 1) + examRecordResponseDTO.getFinalScore()) / gradeStatistic.getNumParticipants();
        gradeStatistic.setAverageScore(avg);
        // 最高分 = 最高分 > 新记录得分 ? 最高分 : 新记录得分
        gradeStatistic.setHighestScore(gradeStatistic.getHighestScore() > examRecordResponseDTO.getFinalScore() ? gradeStatistic.getHighestScore() : examRecordResponseDTO.getFinalScore());
        // 找到新纪录成绩所在区间该区间，并更新该区间人数
        //把裸 List<?> 里的 LinkedHashMap 手动变成 DTO
        List<?> rawList = gradeStatistic.getScoreDistribution();
        List<ScoreDistributionEntryDTO> distList = new ArrayList<>(rawList.size());
        for (Object obj : rawList) {
            Map<String, Object> map = (Map<String, Object>) obj;
            ScoreDistributionEntryDTO dto = new ScoreDistributionEntryDTO();
            dto.setLowerBound(((Number) map.get("lowerBound")).intValue());
            dto.setUpperBound(((Number) map.get("upperBound")).intValue());
            dto.setCount(((Number) map.get("count")).intValue());
            distList.add(dto);
        }
        //按分数段累加
        distList.forEach(dto -> {
            if (dto.getLowerBound() <= examRecordResponseDTO.getFinalScore()
                    && dto.getUpperBound() >= examRecordResponseDTO.getFinalScore()) {
                dto.setCount(dto.getCount() + 1);
            }
        });
        //把改好的列表放回实体
        gradeStatistic.setScoreDistribution(distList);
        if (gradeStatistic.getId() == null) {
            gradesMapper.insert(gradeStatistic);
        } else {
            gradesMapper.updateById(gradeStatistic);
        }
    }
}
