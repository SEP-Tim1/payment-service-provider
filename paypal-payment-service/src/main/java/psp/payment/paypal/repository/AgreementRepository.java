package psp.payment.paypal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.payment.paypal.model.PayPalAgreement;

@Repository
public interface AgreementRepository extends JpaRepository<PayPalAgreement, Long> {

    PayPalAgreement findByRequestId(long requestId);
}
