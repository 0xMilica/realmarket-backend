package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Shareholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShareholderRepository extends JpaRepository<Shareholder, Long> {
  Optional<Shareholder> findById(Long id);

  List<Shareholder> findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(String campaignName);

  Integer countByCampaignUrlFriendlyName(String campaignName);

  @Query("Select MAX(ci.orderNumber) FROM Shareholder ci WHERE ci.campaign.urlFriendlyName = ?1")
  Integer getMaxOrder(String urlFriendlyName);
}
