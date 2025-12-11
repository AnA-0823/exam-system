package priv.ana.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import priv.ana.core.web.domain.Response;
import priv.ana.exception.EmptyResultException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Response<Void> handleException(EmptyResultException e) {
        log.warn(e.getMessage());
        return Response.success(null);
    }
}
