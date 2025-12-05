package priv.ana.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.ana.core.web.domain.Response;
import priv.ana.dto.UserLoginDTO;
import priv.ana.dto.UserRegisterDTO;
import priv.ana.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Response<String> login(@RequestBody UserLoginDTO dto) {
        String token = userService.login(dto);
        return Response.success(token);
    }

    @PostMapping("/register")
    public Response<String> register(@RequestBody UserRegisterDTO dto) {
        String token = userService.register(dto);
        return Response.success(token);
    }
}
