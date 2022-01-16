package psp.payment.paypal.service.paypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.paypal.model.PayPalAgreement;
import psp.payment.paypal.repository.AgreementRepository;
import psp.payment.paypal.service.AgreementService;

@Service
public class PayPalAgreementService implements AgreementService {

    @Autowired
    AgreementRepository repository;

    @Override
    public void save(PayPalAgreement agreement) {
        PayPalAgreement stored = repository.findByRequestId(agreement.getRequestId());
        if (stored != null) {
            stored.setStatus(agreement.getStatus());
            repository.save(stored);
        } else {
            repository.save(agreement);
        }
    }

    @Override
    public PayPalAgreement findByRequestId(long requestId) {
        return repository.findByRequestId(requestId);
    }
}
