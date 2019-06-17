package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.enums.RequestStateName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestStateRepository extends JpaRepository<RequestState, Long> {
  Optional<RequestState> findByName(RequestStateName name);
}
