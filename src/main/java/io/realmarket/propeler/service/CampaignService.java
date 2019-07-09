package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.Company;
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

  void throwIfNoAccess(Campaign campaign);

  void throwIfNotEditable(Campaign campaign);

  void throwIfNotActive(Campaign campaign);

  Campaign getActiveCampaignForCompany();

  CampaignResponseDto getActiveCampaignDto();

  CampaignResponseDto getCampaignDtoByUrlFriendlyName(String name);

  AuditCampaignResponseDto getAuditCampaign(String campaignName);

  Campaign getCampaignByUrlFriendlyName(String name);

  void delete(String campaignName, TwoFADto twoFADto) throws AuthenticationException;

  Campaign changeCampaignStateOrThrow(Campaign campaign, CampaignState followingCampaignState);

  void requestReviewForCampaign(String campaignName);

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
}
