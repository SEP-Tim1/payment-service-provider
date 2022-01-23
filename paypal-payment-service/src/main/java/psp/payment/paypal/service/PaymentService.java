package psp.payment.paypal.service;

import psp.payment.paypal.dto.PaymentRequestDTO;
import psp.payment.paypal.exceptions.AlreadyProcessedException;
import psp.payment.paypal.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface PaymentService {

    String pay(HttpServletRequest r, PaymentRequestDTO request) throws NotFoundException, AlreadyProcessedException;

    String executePayment(HttpServletRequest r, String paypalPaymentId, String payerId) throws NotFoundException;

    String cancelPayment(HttpServletRequest r, PaymentRequestDTO request) throws NotFoundException;

    boolean isEnabledForRequest(long requestId, long storeId);

    String executeAgreement(HttpServletRequest r, PaymentRequestDTO request, String token);

    String cancelAgreement(HttpServletRequest r, PaymentRequestDTO request) throws NotFoundException;
}
