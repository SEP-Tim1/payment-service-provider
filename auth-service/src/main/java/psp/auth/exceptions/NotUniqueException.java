package psp.auth.exceptions;

import org.springframework.http.HttpStatus;

public class NotUniqueException extends BaseException{

    public NotUniqueException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getResponseStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
