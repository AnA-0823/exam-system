package priv.ana.service;

import org.springframework.web.bind.annotation.RequestBody;
import priv.ana.dto.UserLoginDTO;
import priv.ana.dto.UserRegisterDTO;

public interface UserService {

    String login(@RequestBody UserLoginDTO dto);

    String register(@RequestBody UserRegisterDTO dto);
}
