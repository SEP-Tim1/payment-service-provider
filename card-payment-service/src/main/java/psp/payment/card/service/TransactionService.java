package psp.payment.card.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.payment.card.dtos.PaymentResponseDTO;
import psp.payment.card.model.Transaction;
import psp.payment.card.repositories.TransactionRepository;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    public void save(PaymentResponseDTO dto) {
        Transaction transaction = new Transaction(
                dto.getTransactionStatus(),
                dto.getMerchantOrderId(),
                dto.getRequestId(),
                dto.getAcquirerOrderId(),
                dto.getAcquirerTimestamp(),
                dto.getPaymentId(),
                dto.getErrorMessage()
        );
        transaction = repository.save(transaction);
        log.info("Transaction id=" + transaction.getId() + " created.");
    }
}
