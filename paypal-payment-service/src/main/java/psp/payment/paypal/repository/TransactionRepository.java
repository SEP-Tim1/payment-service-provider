package psp.payment.paypal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.payment.paypal.model.PayPalTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<PayPalTransaction, Long> {

    PayPalTransaction findByPaypalPaymentId(String paypalPaymentId);

    PayPalTransaction findByRequestId(long requestId);
}
