package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignInvestmentRepository extends JpaRepository<CampaignInvestment, Long> {}
