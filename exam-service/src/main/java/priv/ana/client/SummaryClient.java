package priv.ana.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import priv.ana.core.web.domain.Response;
import priv.ana.core.web.domain.dtos.examServiceDTO.ExamRecordResponseDTO;
import priv.ana.core.web.domain.dtos.summaryServiceDTO.GradeStatisticsCreateRequestDTO;

@FeignClient(name = "summary-service",path = "grades")
public interface SummaryClient {
    /**
     * 更新成绩统计
     * @param examRecordResponseDTO
     * @return
     */
    @PutMapping
    public Response<Void> updateGradeStatistics(@RequestBody ExamRecordResponseDTO examRecordResponseDTO);
}
