package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Company;
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
      "select c, s from Campaign c join fetch c.campaignState s where c.company = :company and s.name <> 'DELETED' and s.name <> 'POST_CAMPAIGN'")
  Optional<Campaign> findExistingByCompany(@Param("company") final Company company);

  List<Campaign> findAllByCompany(Company company);
}
