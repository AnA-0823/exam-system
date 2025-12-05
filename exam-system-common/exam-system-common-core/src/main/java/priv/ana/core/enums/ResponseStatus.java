package priv.ana.core.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "bad request"),
    UNAUTHORIZED(401, "unauthorized"),
    INTERNAL_SERVER_ERROR(500, "internal server error"),
    ;

    private final Integer code;
    private final String msg;

    private ResponseStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
