package psp.payment.paypal.service;

import psp.payment.paypal.model.PayPalAgreement;

public interface AgreementService {

    void save(PayPalAgreement agreement);

    PayPalAgreement findByRequestId(long requestId);
}
