package psp.payment.bitcoin.service;

import psp.payment.bitcoin.dto.ChargeStatusDTO;
import psp.payment.bitcoin.dto.PaymentRequestDTO;
import psp.payment.bitcoin.exceptions.NotFoundException;

public interface PaymentService {

    String createCharge(PaymentRequestDTO request) throws NotFoundException;

    void processChargeStatus(ChargeStatusDTO chargeStatus, long requestId);
}
