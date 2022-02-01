package psp.auth.exceptions;

import org.springframework.http.HttpStatus;

public class AccountBlockedException extends BaseException {

    public AccountBlockedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getResponseStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
