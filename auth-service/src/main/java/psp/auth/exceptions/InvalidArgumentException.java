package psp.auth.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends BaseException {

    public InvalidArgumentException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getResponseStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
