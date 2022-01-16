package psp.payment.paypal.service;

import com.paypal.base.rest.PayPalRESTException;
import psp.payment.paypal.dto.PaymentRequestDTO;
import psp.payment.paypal.exceptions.AlreadyProcessedException;
import psp.payment.paypal.exceptions.NotFoundException;

public interface PaymentService {

    String pay(PaymentRequestDTO request) throws NotFoundException, AlreadyProcessedException, PayPalRESTException;

    String executePayment(String paypalPaymentId, String payerId) throws NotFoundException;

    String cancelPayment(PaymentRequestDTO request) throws NotFoundException;

    boolean isEnabledForRequest(long requestId, long storeId);

    String executeAgreement(PaymentRequestDTO request, String token);

    String cancelAgreement(PaymentRequestDTO request) throws NotFoundException;
}
