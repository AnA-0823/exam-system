package priv.ana.core.web.domain;

import lombok.Data;
import priv.ana.core.constant.ResponseStatus;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private T data;

    public static Response<Void> success() {
        return new Response<>(ResponseStatus.SUCCESS);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(ResponseStatus.SUCCESS, data);
    }

    public static Response<Void> fail(ResponseStatus status) {
        return new Response<>(status);
    }

    public static <T> Response<T> fail(ResponseStatus status, T data) {
        return new Response<>(status, data);
    }

    public static <T> Response<T> fail(ResponseStatus status, String msg, T data) {
        return new Response<>(status.getCode(), msg, data);
    }

    private Response(ResponseStatus responseStatus) {
        this.code = responseStatus.getCode();
        this.msg = responseStatus.getMsg();
    }

    private Response(ResponseStatus responseStatus, T data) {
        this.code = responseStatus.getCode();
        this.msg = responseStatus.getMsg();
        this.data = data;
    }

    private Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
