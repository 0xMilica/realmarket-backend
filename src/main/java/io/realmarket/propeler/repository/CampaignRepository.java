package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

  @Query(
      "select c, s from Campaign c join fetch c.campaignState s where c.urlFriendlyName = :urlFriendlyName and s.name <> 'DELETED' ")
  Optional<Campaign> findByUrlFriendlyNameAndDeletedFalse(
      @Param("urlFriendlyName") String urlFriendlyName);

  @Query(
      "select c, s from Campaign c join fetch c.campaignState s where c.company = :company and s.name = 'ACTIVE' ")
  Optional<Campaign> findByCompanyAndActiveTrue(@Param("company") final Company company);

  @Query(
      "select c, s from Campaign c join fetch c.campaignState s where c.company = :company and s.name <> 'DELETED' and s.name <> 'SUCCESSFUL' and s.name <> 'UNSUCCESSFUL'")
  Optional<Campaign> findExistingByCompany(@Param("company") final Company company);

  @Query(
      value =
          "SELECT c FROM Campaign c LEFT JOIN CampaignState s ON c.campaignState.id = s.id WHERE s.name = 'ACTIVE' OR s.name = 'SUCCESSFUL'",
      countQuery =
          "SELECT COUNT(c) FROM Campaign c LEFT JOIN CampaignState s ON c.campaignState.id = s.id WHERE s.name = 'ACTIVE' OR s.name = 'SUCCESSFUL'")
  Page<Campaign> findAllPublic(Pageable pageable);

  @Query(
      value =
          "SELECT c FROM Campaign c LEFT JOIN CampaignState s ON c.campaignState.id = s.id WHERE :state is null or s = :state")
  Page<Campaign> findAllByCampaignState(Pageable pageable, @Param("state") CampaignState state);

  @Query(
      value =
          "SELECT c FROM Campaign c LEFT JOIN CampaignState s ON c.campaignState.id = s.id WHERE s.name = 'SUCCESSFUL' OR s.name = 'UNSUCCESSFUL'")
  Page<Campaign> findAllCompletedCampaigns(Pageable pageable);

  Page<Campaign> findAllByCampaignStateAndCompany(
      Pageable pageable, CampaignState state, Company company);

  List<Campaign> findAllByCampaignState(CampaignState state);

  @Query(
      "SELECT c FROM Campaign c LEFT JOIN CampaignState s ON c.campaignState.id = s.id WHERE s.name = 'AUDIT' AND c.id IN (SELECT a.campaign FROM Audit a WHERE auditor = :auth)")
  Page<Campaign> findAuditCampaigns(@Param("auth") Auth auth, Pageable pageable);

  List<Campaign> findAllByCompany(Company company);

  Page<Campaign> findAllByCompany(Pageable pageable, Company company);

  @Query(
      value =
          "SELECT c FROM Campaign c JOIN c.campaignState s WHERE c.company = :company AND s.name <> 'DELETED' ORDER BY c.creationDate DESC")
  Page<Campaign> findAllByCompanyViewable(Pageable pageable, @Param("company") Company company);

  @Query(
      "select c from Campaign c join fetch c.campaignState s where c.company = :company and s.name <> 'DELETED' order by c.creationDate desc")
  List<Campaign> findByCompany(@Param("company") Company company);
}
