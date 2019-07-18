package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.enums.CampaignStateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

  List<Investment> findAllByCampaign(Campaign campaign);

  List<Investment> findAllByCampaignAndAuth(Campaign campaign, Auth auth);

  @Query(
      "SELECT c FROM Campaign c WHERE id IN (SELECT i.campaign FROM Investment i WHERE auth = :auth) ORDER BY modified_date DESC")
  Page<Campaign> findInvestedCampaign(@Param("auth") Auth auth, Pageable pageable);

  @Query(
      "SELECT c FROM Campaign c LEFT JOIN CampaignState s ON c.campaignState.id = s.id WHERE s.name = :state AND c.id IN (SELECT i.campaign FROM Investment i WHERE auth = :auth) ORDER BY modified_date DESC")
  Page<Campaign> findInvestedCampaignByState(
      @Param("auth") Auth auth, @Param("state") CampaignStateName state, Pageable pageable);
}
