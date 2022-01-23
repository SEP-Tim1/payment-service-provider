package psp.payment.qrcode.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getResponseStatusCode() {
        return HttpStatus.FORBIDDEN;
    }
}
