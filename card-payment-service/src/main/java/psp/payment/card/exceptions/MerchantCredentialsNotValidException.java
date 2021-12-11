package psp.payment.card.exceptions;

public class MerchantCredentialsNotValidException extends Exception {
    private static final String message = "Invalid credentials!";

    public MerchantCredentialsNotValidException() {
        super(message);
    }
}
