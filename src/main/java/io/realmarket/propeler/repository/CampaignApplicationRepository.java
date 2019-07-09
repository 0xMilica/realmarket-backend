package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignApplicationRepository extends JpaRepository<CampaignApplication, Long> {}
