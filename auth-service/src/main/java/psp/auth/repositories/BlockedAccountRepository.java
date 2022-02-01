package psp.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.auth.model.BlockedAccount;

import java.util.List;

@Repository
public interface BlockedAccountRepository extends JpaRepository<BlockedAccount, Long> {

    List<BlockedAccount> findAllByUserId(long userId);
}
