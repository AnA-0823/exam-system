package priv.ana.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.annotations.*;
import priv.ana.pojo.entity.Question;
import priv.ana.pojo.vo.QuestionResponseVO;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
    @Select("SELECT * FROM questions WHERE exam_id = #{examId}")
    @Results({
            @Result(property = "options", column = "options_json",
                    typeHandler = JacksonTypeHandler.class)
    })
    List<Question> selectQuestionWithOptions(@Param("examId") Long examId);
}
