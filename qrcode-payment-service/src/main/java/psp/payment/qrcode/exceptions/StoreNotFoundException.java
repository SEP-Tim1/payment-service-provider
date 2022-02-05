package psp.payment.qrcode.exceptions;

public class StoreNotFoundException extends Exception{
    private static final String message = "Store not found!";

    public StoreNotFoundException() {
        super(message);
    }
}
