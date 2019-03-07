package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampaignTeamMemberRepository extends JpaRepository<CampaignTeamMember, Long> {
  List<CampaignTeamMember> findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(
      String campaignName);

  Integer countByCampaignUrlFriendlyName(String campaignName);

  @Modifying
  @Query("update CampaignTeamMember ctm set ctm.orderNumber = ?2 where ctm.id=?1")
  void reorderTeamMembers(Long id, Integer orderNumber);
}
