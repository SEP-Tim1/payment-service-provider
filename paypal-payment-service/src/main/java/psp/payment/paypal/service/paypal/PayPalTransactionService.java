package psp.payment.paypal.service.paypal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.paypal.model.PayPalTransaction;
import psp.payment.paypal.repository.TransactionRepository;
import psp.payment.paypal.service.TransactionService;
import psp.payment.paypal.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class PayPalTransactionService implements TransactionService {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private LoggerUtil loggerUtil;

    @Override
    public PayPalTransaction save(HttpServletRequest request, long requestId, String paypalPaymentId, String status) {
        PayPalTransaction transaction = repository.findByPaypalPaymentId(paypalPaymentId);
        if (transaction == null) {
            transaction = new PayPalTransaction(
                    requestId, paypalPaymentId, status
            );
        } else {
            transaction.setStatus(status);
        }
        transaction = repository.save(transaction);
        log.info(loggerUtil.getLogMessage(request, "Transaction (id=" + transaction.getId() + ", requestId=" + transaction.getRequestId() + ") status " + transaction.getStatus()));
        return transaction;
    }

    @Override
    public PayPalTransaction findByPayPalPaymentId(String paypalPaymentId) {
        return repository.findByPaypalPaymentId(paypalPaymentId);
    }

    @Override
    public PayPalTransaction findByRequestId(long requestId) {
        return repository.findByRequestId(requestId);
    }
}
