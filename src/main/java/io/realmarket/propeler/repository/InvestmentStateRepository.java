package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.InvestmentState;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentStateRepository extends JpaRepository<InvestmentState, Long> {
  Optional<InvestmentState> findByName(InvestmentStateName name);
}
