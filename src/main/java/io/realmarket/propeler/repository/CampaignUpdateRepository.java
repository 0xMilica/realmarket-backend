package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface CampaignUpdateRepository extends JpaRepository<CampaignUpdate, Long> {
    Page<CampaignUpdate> findByCampaign(Campaign campaign, Pageable pageable);
}
