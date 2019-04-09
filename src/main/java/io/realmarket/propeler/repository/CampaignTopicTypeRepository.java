package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignTopicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignTopicTypeRepository extends JpaRepository<CampaignTopicType, Long> {
  Optional<CampaignTopicType> findByNameIgnoreCase(String name);
}
