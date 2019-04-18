package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
  Optional<Campaign> findByUrlFriendlyName(String urlFriendlyName);

  Optional<Campaign> findByCompanyIdAndActiveTrue(final Long companyId);
}
