package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignInvestor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignInvestorRepository extends JpaRepository<CampaignInvestor, Long> {
  Optional<CampaignInvestor> findById(Long id);

  List<CampaignInvestor> findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(String campaignName);

  Integer countByCampaignUrlFriendlyName(String campaignName);

}
