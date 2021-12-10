package psp.payment.card.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.payment.card.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByStoreId(long storeId);
}
