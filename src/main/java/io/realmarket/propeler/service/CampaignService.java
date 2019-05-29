package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Campaign;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;

public interface CampaignService {
  Campaign findByUrlFriendlyNameOrThrowException(String name);

  void createCampaign(CampaignDto campaignDto);

  CampaignDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto);

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
}
