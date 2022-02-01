package psp.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psp.auth.model.FailedLoginAttempt;

import java.util.List;

@Repository
public interface LoginAttemptRepository extends JpaRepository<FailedLoginAttempt, Long> {

    List<FailedLoginAttempt> findAllByUserId(long userId);
}
