package psp.payment.bitcoin.service.opennode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.bitcoin.model.Transaction;
import psp.payment.bitcoin.repository.TransactionRepository;
import psp.payment.bitcoin.service.TransactionService;
import psp.payment.bitcoin.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class OpenNodeTransactionService implements TransactionService {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private LoggerUtil loggerUtil;

    @Override
    public Transaction save(HttpServletRequest request, long requestId, long merchantOrderId, String status) {
        Transaction transaction = repository.findByRequestId(requestId);
        if (transaction != null) {
            transaction.setStatus(status);
        } else {
            transaction = new Transaction(requestId, merchantOrderId, status);
        }
        transaction = repository.save(transaction);
        log.info(loggerUtil.getLogMessage(request, "Transaction (id=" + transaction.getId() + ", requestId=" + transaction.getRequestId() + ") status set to " + transaction.getStatus()));
        return transaction;
    }

    @Override
    public boolean exists(long requestId) {
        return repository.findByRequestId(requestId) != null;
    }
}
