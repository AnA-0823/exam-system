package priv.ana.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserStatusEnum {

    ACTIVE(1, "激活"),
    DISABLE(0, "禁用");

    @EnumValue
    private final Integer code;
    @JsonValue
    private final String desc;

    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
