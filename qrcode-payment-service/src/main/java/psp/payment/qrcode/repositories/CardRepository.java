package psp.payment.qrcode.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.payment.qrcode.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByStoreId(long storeId);
}
