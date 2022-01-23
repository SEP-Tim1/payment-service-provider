package psp.payment.paypal.service;

import psp.payment.paypal.model.PayPalTransaction;

import javax.servlet.http.HttpServletRequest;

public interface TransactionService {

    PayPalTransaction save(HttpServletRequest request, long requestId, String paypalPaymentId, String status);

    PayPalTransaction findByPayPalPaymentId(String paypalPaymentId);

    PayPalTransaction findByRequestId(long requestId);
}
