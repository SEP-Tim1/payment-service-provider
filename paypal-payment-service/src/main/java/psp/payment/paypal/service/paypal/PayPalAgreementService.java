package psp.payment.paypal.service.paypal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.paypal.model.PayPalAgreement;
import psp.payment.paypal.repository.AgreementRepository;
import psp.payment.paypal.service.AgreementService;
import psp.payment.paypal.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class PayPalAgreementService implements AgreementService {

    @Autowired
    private AgreementRepository repository;
    @Autowired
    private LoggerUtil loggerUtil;

    @Override
    public void save(HttpServletRequest r, PayPalAgreement agreement) {
        PayPalAgreement stored = repository.findByRequestId(agreement.getRequestId());
        if (stored != null) {
            stored.setStatus(agreement.getStatus());
            stored = repository.save(stored);
        } else {
            stored = repository.save(agreement);
        }
        log.info(loggerUtil.getLogMessage(r, "Agreement (id=" + stored.getId() + ", requestId=" + stored.getRequestId() + ") status " + stored.getStatus()));
    }

    @Override
    public PayPalAgreement findByRequestId(long requestId) {
        return repository.findByRequestId(requestId);
    }
}
