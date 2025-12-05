package priv.ana.service.impl;

import org.springframework.stereotype.Service;
import priv.ana.core.enums.UserRoleEnum;
import priv.ana.core.enums.UserStatusEnum;
import priv.ana.core.utils.JwtUtils;
import priv.ana.core.web.domain.entities.User;
import priv.ana.dto.UserLoginDTO;
import priv.ana.dto.UserRegisterDTO;
import priv.ana.repository.UserRepository;
import priv.ana.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String login(UserLoginDTO dto) {
        String hashPassword = getHashPassword(dto.getPassword());
        User user = userRepository.getUserByUsernameAndPassword(dto.getUsername(), hashPassword);
        if (user == null) {
            throw new RuntimeException("用户或密码名错误");
        }
        HashMap<String, String> payload = new HashMap<>();
        payload.put("userId", user.getId().toString());
        payload.put("role", user.getRole().getDesc());
        return JwtUtils.build(payload);
    }

    @Override
    public String register(UserRegisterDTO dto) {
        String hashPassword = getHashPassword(dto.getPassword());
        User user = new User()
                .setUsername(dto.getUsername())
                .setPassword(hashPassword)
                .setRole(UserRoleEnum.STUDENT)
                .setStatus(UserStatusEnum.ACTIVE);
        userRepository.save(user);
        HashMap<String, String> payload = new HashMap<>();
        payload.put("userId", user.getId().toString());
        payload.put("role", user.getRole().getDesc());
        return JwtUtils.build(payload);
    }


    private String getHashPassword(String password) {
        String hashPassword;
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] hash = instance.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            hashPassword = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return hashPassword;
    }
}
