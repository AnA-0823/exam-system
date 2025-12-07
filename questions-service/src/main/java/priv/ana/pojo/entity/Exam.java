package priv.ana.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷实体
 */
@Data
@TableName("exams")
public class Exam {
    /**
     * 试卷ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 试卷标题
     */
    private String title;
    /**
     * 试卷总分
     */
    private Double totalScore;
    /**
     * 考试时长
     */
    private Long duration;
    /**
     * 考试结束时间
     */
    private LocalDateTime endTime;
    /**
     * 考试开始时间
     */
    private LocalDateTime startTime;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;
}
