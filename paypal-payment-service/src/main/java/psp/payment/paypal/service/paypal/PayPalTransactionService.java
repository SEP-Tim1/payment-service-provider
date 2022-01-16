package psp.payment.paypal.service.paypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.paypal.model.PayPalTransaction;
import psp.payment.paypal.repository.TransactionRepository;
import psp.payment.paypal.service.TransactionService;

@Service
public class PayPalTransactionService implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Override
    public PayPalTransaction save(long requestId, String paypalPaymentId, String status) {
        PayPalTransaction transaction = repository.findByPaypalPaymentId(paypalPaymentId);
        if (transaction == null) {
            transaction = new PayPalTransaction(
                    requestId, paypalPaymentId, status
            );
        } else {
            transaction.setStatus(status);
        }
        transaction = repository.save(transaction);
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
