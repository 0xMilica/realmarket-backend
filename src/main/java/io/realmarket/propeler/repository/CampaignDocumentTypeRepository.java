package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignDocumentType;
import io.realmarket.propeler.model.enums.ECampaignDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CampaignDocumentTypeRepository extends JpaRepository<CampaignDocumentType, Long> {
  Optional<CampaignDocumentType> findByName(ECampaignDocumentType name);
}
