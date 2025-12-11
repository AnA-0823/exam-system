package priv.ana.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import priv.ana.pojo.entity.GradeStatistic;

@Mapper
public interface GradesMapper extends BaseMapper<GradeStatistic> {
}
