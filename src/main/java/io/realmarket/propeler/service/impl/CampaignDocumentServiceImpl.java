package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.repository.CampaignDocumentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignDocumentService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CampaignDocumentServiceImpl implements CampaignDocumentService {

  private final CampaignDocumentRepository campaignDocumentRepository;
  private final CampaignService campaignService;

  @Autowired
  public CampaignDocumentServiceImpl(
      CampaignDocumentRepository campaignDocumentRepository, CampaignService campaignService) {
    this.campaignDocumentRepository = campaignDocumentRepository;
    this.campaignService = campaignService;
  }

  @Transactional
  public CampaignDocument submitDocument(
      CampaignDocument campaignDocument, String campaignUrlFriendlyName) {
    Campaign campaign =
        campaignService.findByUrlFriendlyNameOrThrowException(campaignUrlFriendlyName);

    if (!campaign.getCompany().getAuth().equals(AuthenticationUtil.getAuthentication().getAuth())) {

      throw new AccessDeniedException(ExceptionMessages.NOT_CAMPAIGN_COMPANY_OWNER);
    }

    campaignDocument.setCampaign(campaign);

    return campaignDocumentRepository.save(campaignDocument);
  }
}
