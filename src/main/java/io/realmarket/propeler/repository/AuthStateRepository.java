package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.AuthState;
import io.realmarket.propeler.model.enums.AuthStateName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthStateRepository extends JpaRepository<AuthState, Long> {
  Optional<AuthState> findByName(AuthStateName name);
}
