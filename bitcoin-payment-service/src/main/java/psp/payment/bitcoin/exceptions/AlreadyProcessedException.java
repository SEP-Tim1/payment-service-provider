package psp.payment.bitcoin.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyProcessedException extends BaseException{

    public AlreadyProcessedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getResponseStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
