package psp.payment.card.exceptions;

public class RequestNotFoundException extends Exception {
    private static final String message = "Payment Request not found!";

    public RequestNotFoundException() {
        super(message);
    }
}
