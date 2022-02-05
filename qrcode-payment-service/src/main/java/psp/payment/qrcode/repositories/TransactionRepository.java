package psp.payment.qrcode.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.payment.qrcode.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByMerchantOrderId(long merchantOrderId);
}
