package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.util.List;

public interface CampaignService {
  Campaign findByUrlFriendlyNameOrThrowException(String name);

  List<Campaign> findAllByCompany(Company company);

  CampaignResponseDto createCampaign(CampaignDto campaignDto);

  CampaignResponseDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto);

  void uploadMarketImage(String campaignName, MultipartFile logo);

  FileDto downloadMarketImage(String campaignName);

  void deleteMarketImage(String campaignName);

  boolean isOwner(Campaign campaign);

  void throwIfNotOwnerOrNotEditable(Campaign campaign);

  void throwIfNoAccess(Campaign campaign);

  void throwIfNotEditable(Campaign campaign);

  Campaign getActiveCampaignForCompany();

  CampaignDto getActiveCampaignDto();

  CampaignDto getCampaignDtoByUrlFriendlyName(String name);

  Campaign getCampaignByUrlFriendlyName(String name);

  void delete(String campaignName, TwoFADto twoFADto) throws AuthenticationException;

  void requestReviewForCampaign(String campaignName);

  Page<CampaignResponseDto> getPublicCampaigns(Pageable pageable, String filter);
}
