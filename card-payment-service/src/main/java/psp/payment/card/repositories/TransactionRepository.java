package psp.payment.card.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.payment.card.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
