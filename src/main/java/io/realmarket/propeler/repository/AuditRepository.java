package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

  @Query(
      "SELECT a FROM Audit a LEFT JOIN RequestState rs ON a.requestState.id = rs.id WHERE a.campaign = :campaign AND rs.name = 'PENDING'")
  Optional<Audit> findPendingAuditByCampaign(@Param("campaign") Campaign campaign);
}
