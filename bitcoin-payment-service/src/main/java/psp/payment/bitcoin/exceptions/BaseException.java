package psp.payment.bitcoin.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends Exception {

    public BaseException(String message) {
        super(message);
    }

    public abstract HttpStatus getResponseStatusCode();
}
