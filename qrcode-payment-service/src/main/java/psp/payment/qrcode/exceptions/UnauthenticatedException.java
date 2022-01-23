package psp.payment.qrcode.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthenticatedException extends BaseException {

    public UnauthenticatedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getResponseStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
