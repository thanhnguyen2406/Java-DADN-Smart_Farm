package dadn_SmartFarm.exception;

import dadn_SmartFarm.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<Response> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        Response response = new Response();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<Response> handleValidation(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode error = ErrorCode.valueOf(enumKey);

        Response response = new Response();
        response.setCode(error.getCode());
        response.setMessage(error.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}
