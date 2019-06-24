package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignUpdateRepository extends JpaRepository<CampaignUpdate, Long> {}
