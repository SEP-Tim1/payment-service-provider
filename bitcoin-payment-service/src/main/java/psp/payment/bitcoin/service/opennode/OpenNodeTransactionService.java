package psp.payment.bitcoin.service.opennode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.bitcoin.model.Transaction;
import psp.payment.bitcoin.repository.TransactionRepository;
import psp.payment.bitcoin.service.TransactionService;

@Service
public class OpenNodeTransactionService implements TransactionService {

    @Autowired
    TransactionRepository repository;

    @Override
    public Transaction save(long requestId, long merchantOrderId, String status) {
        Transaction transaction = repository.findByRequestId(requestId);
        if (transaction != null) {
            transaction.setStatus(status);
        } else {
            transaction = new Transaction(requestId, merchantOrderId, status);
        }
        transaction = repository.save(transaction);
        return transaction;
    }

    @Override
    public boolean exists(long requestId) {
        return repository.findByRequestId(requestId) != null;
    }
}
