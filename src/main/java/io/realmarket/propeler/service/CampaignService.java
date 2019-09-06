package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.enums.CampaignStateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;
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

  void throwIfNotOwner(Campaign campaign);

  void throwIfNoAccess(Campaign campaign);

  void throwIfNotEditable(Campaign campaign);

  void throwIfNotActive(Campaign campaign);

  Campaign getActiveCampaign();

  CampaignResponseDto getCurrentCampaignDto();

  CampaignResponseDto getCampaignDtoByUrlFriendlyName(String name);

  Campaign getCampaignByUrlFriendlyName(String name);

  void delete(String campaignName, TwoFADto twoFADto) throws AuthenticationException;

  void requestReviewForCampaign(String campaignName);

  Campaign launchCampaign(String campaignName);

  Campaign closeCampaign(String campaignName, CampaignClosingReasonDto campaignClosingReasonDto);

  Campaign changeCampaignStateOrThrow(Campaign campaign, CampaignStateName followingCampaignState);

  List<Campaign> findByCompany(Company company);

  List<CampaignResponseDto> getAllCampaignsForUser();

  Page<CampaignResponseDto> getPublicCampaigns(Pageable pageable, String filter);

  Page<CampaignResponseDto> getCampaignsByState(Pageable pageable, String state);

  void sendNewCampaignOpportunityEmail(Campaign campaign);

  void sendNewCampaignOpportunitiesEmail();

  void increaseCollectedAmount(Campaign campaign, BigDecimal amountOfMoney);

  void decreaseCollectedAmount(Campaign campaign, BigDecimal amountOfMoney);

  BigDecimal getMaximumInvestableAmount(Campaign campaign);

  BigDecimal getMaximumAcquirableEquity(Campaign campaign);

  BigDecimal convertMoneyToPercentageOfEquity(String campaignName, BigDecimal money);

  BigDecimal convertPercentageOfEquityToMoney(String campaignName, BigDecimal percentageOfEquity);

  BigDecimal getAvailableEquity(String campaignName);

  BigDecimal getAvailableInvestableAmount(String campaignName);

  AvailableInvestmentDto getAvailableInvestment(String campaignName);

  Page<CampaignWithInvestmentsWithPersonResponseDto> getCampaignsByStateWithInvestments(
      Pageable pageable, String state);
}
