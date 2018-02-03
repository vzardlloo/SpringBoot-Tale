package boot.tale.exception;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GolbalExceptionHandler {

    @ExceptionHandler(value = TaleException.class)
    public TaleException golbalExceptionHandler(TaleException e){
            return e;
    }

}
