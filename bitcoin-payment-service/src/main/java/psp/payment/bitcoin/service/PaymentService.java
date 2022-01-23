package psp.payment.bitcoin.service;

import psp.payment.bitcoin.dto.ChargeStatusDTO;
import psp.payment.bitcoin.dto.PaymentRequestDTO;
import psp.payment.bitcoin.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface PaymentService {

    String createCharge(HttpServletRequest r, PaymentRequestDTO request) throws NotFoundException;

    void processChargeStatus(HttpServletRequest request, ChargeStatusDTO chargeStatus, long requestId);

    boolean isEnabledForRequest(long requestId, long storeId);
}
