package psp.payment.paypal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.payment.paypal.model.MerchantSubscription;

@Repository
public interface MerchantSubscriptionRepository extends JpaRepository<MerchantSubscription, Long> {

    MerchantSubscription findByStoreId(long storeId);
}
