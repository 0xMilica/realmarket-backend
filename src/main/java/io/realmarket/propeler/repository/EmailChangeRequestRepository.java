package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.EmailChangeRequest;
import io.realmarket.propeler.model.TemporaryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailChangeRequestRepository extends JpaRepository<EmailChangeRequest, Long> {

  Optional<EmailChangeRequest> findByTemporaryToken(final TemporaryToken token);
}
