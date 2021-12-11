package psp.payment.card.exceptions;

public class InvoiceNotValidException extends Exception {
    private static final String message = "Invoice is not valid!";

    public InvoiceNotValidException() {
        super(message);
    }
}
