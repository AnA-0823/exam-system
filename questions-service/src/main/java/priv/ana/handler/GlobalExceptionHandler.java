package priv.ana.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import priv.ana.core.constant.ResponseStatus;
import priv.ana.core.web.domain.Response;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Response handleAll(Exception e){
        return Response.fail(ResponseStatus.INTERNAL_SERVER_ERROR,e.getMessage(),null);
    }
}
