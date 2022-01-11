package psp.payment.bitcoin.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import psp.payment.bitcoin.exceptions.BaseException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    private static final String GENERIC_ERROR_RESPONSE = "Something went wrong";

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<String> handleBaseException(BaseException e) {
        return new ResponseEntity<>(e.getMessage(), e.getResponseStatusCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Unhandled exception", e);
        e.printStackTrace();
        return new ResponseEntity<>(GENERIC_ERROR_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
