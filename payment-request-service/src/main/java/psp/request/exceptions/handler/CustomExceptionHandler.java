package psp.request.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import psp.request.exceptions.BaseException;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final String GENERIC_ERROR_RESPONSE = "Something went wrong";

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<String> handleBaseException(BaseException e) {
        return new ResponseEntity<>(e.getMessage(), e.getResponseStatusCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(GENERIC_ERROR_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}