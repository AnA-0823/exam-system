package priv.ana.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import priv.ana.core.web.domain.entities.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
