package priv.ana.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserRoleEnum {

    STUDENT(1, "学生"),
    TEACHER(2, "老师");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    UserRoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Integer getCode(String userRole) {
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.getDesc().equals(userRole)) {
                return userRoleEnum.code;
            }
        }
        return null;
    }

}
