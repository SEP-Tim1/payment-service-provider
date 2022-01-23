package psp.payment.paypal.service;

import psp.payment.paypal.model.PayPalAgreement;

import javax.servlet.http.HttpServletRequest;

public interface AgreementService {

    void save(HttpServletRequest r, PayPalAgreement agreement);

    PayPalAgreement findByRequestId(long requestId);
}
