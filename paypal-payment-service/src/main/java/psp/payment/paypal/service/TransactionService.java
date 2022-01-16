package psp.payment.paypal.service;

import psp.payment.paypal.model.PayPalTransaction;

public interface TransactionService {

    PayPalTransaction save(long requestId, String paypalPaymentId, String status);

    PayPalTransaction findByPayPalPaymentId(String paypalPaymentId);

    PayPalTransaction findByRequestId(long requestId);
}
