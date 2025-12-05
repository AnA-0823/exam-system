package priv.ana.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import priv.ana.core.web.domain.entities.User;
import priv.ana.mapper.UserMapper;

@Repository
public class UserRepository extends ServiceImpl<UserMapper, User> {

    public User getUserByUsernameAndPassword(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
                .eq(User::getPassword, password);
        return getOne(wrapper);
    }
}
