package psp.payment.qrcode.exceptions;

public class QRCodeNotGeneratedException extends Exception {
    private static final String message = "QR Code could not be generated!";

    public QRCodeNotGeneratedException() {
        super(message);
    }
}
