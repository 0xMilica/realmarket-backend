package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.api.dto.CampaignDocumentResponseDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CampaignDocumentService {

  CampaignDocument submitDocument(
      CampaignDocumentDto campaignDocumentDto, String campaignUrlFriendlyName);

  void deleteDocument(Long documentId);

  Map<String, List<CampaignDocumentResponseDto>> getAllCampaignDocumentDtoGroupedByType(
      String campaignName);

  CampaignDocument findByIdOrThrowException(Long documentId);

  List<CampaignDocument> findAllByCampaign(Campaign campaign);

  List<CampaignDocument> findAllByCampaigns(List<Campaign> campaigns);

  Page<CampaignDocument> findAllPageableByCampaigns(List<Campaign> campaigns, Pageable pageable);

  List<CampaignDocument> findAllByCampaignOrderByUploadDateDesc(Campaign campaign);

  CampaignDocument patchCampaignDocument(Long documentId, CampaignDocumentDto campaignDocumentDto);

  List<CampaignDocumentResponseDto> getUserCampaignDocuments(Long userId);

  Page<CampaignDocumentResponseDto> getPageableUserCampaignDocuments(
      Long userId, Pageable pageable);

  List<CampaignDocumentResponseDto> getCampaignDocuments(String campaignName);
}
