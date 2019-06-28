package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignUpdate;
import io.realmarket.propeler.model.enums.CampaignStateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignUpdateRepository extends JpaRepository<CampaignUpdate, Long> {

  @Query("SELECT cu FROM Campaign_update cu ORDER BY post_date DESC")
  Page<CampaignUpdate> findCampaignUpdates(Pageable pageable);

  @Query(
      "SELECT cu FROM Campaign_update cu WHERE campaign IN (SELECT i.campaign FROM Investment i WHERE auth = :auth) ORDER BY post_date DESC")
  Page<CampaignUpdate> findMyCampaignUpdates(@Param("auth") Auth auth, Pageable pageable);

  @Query(
      "SELECT cu FROM Campaign_update cu WHERE campaign IN (SELECT c FROM Campaign c LEFT JOIN CampaignState s ON c.campaignState.id = s.id WHERE s.name = :state) ORDER BY post_date DESC")
  Page<CampaignUpdate> findCampaignUpdatesByCampaignState(
      @Param("state") CampaignStateName campaignState, Pageable pageable);

  Page<CampaignUpdate> findByCampaign(Campaign campaign, Pageable pageable);
}
