package psp.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.store.model.Store;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Store findByUserId(long userId);
    Optional<Store> getByApiToken(String apiToken);
    Optional<Store> getByUserId(long userId);
}
