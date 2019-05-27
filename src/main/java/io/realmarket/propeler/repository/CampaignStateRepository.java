package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignStateRepository  extends JpaRepository<CampaignState, Long> {
    Optional<CampaignState> findByName(CampaignStateName name);
}
