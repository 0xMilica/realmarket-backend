package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.exception.CampaignNameAlreadyExistsException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.CAMPAIGN_NOT_FOUND;

@Slf4j
@Service
public class CampaignServiceImpl implements CampaignService {

  private final CampaignRepository campaignRepository;

  private final CompanyService companyService;

  private final ModelMapperBlankString modelMapperBlankString;

  @Autowired
  public CampaignServiceImpl(
      CampaignRepository campaignRepository,
      CompanyService companyService,
      ModelMapperBlankString modelMapperBlankString) {
    this.campaignRepository = campaignRepository;
    this.companyService = companyService;
    this.modelMapperBlankString = modelMapperBlankString;
  }

  @Override
  public Campaign findByUrlFriendlyNameOrThrowException(String urlFriendlyName) {
    return campaignRepository
        .findByUrlFriendlyName(urlFriendlyName)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_NOT_FOUND));
  }

  public void createCampaign(CampaignDto campaignDto) {
    Company company = companyService.findByIdOrThrowException(campaignDto.getCompanyId());
    if (campaignRepository.findByUrlFriendlyName(campaignDto.getUrlFriendlyName()).isPresent()) {
      log.error("Campaign with the provided name '{}' already exists!", campaignDto.getName());
      throw new CampaignNameAlreadyExistsException(ExceptionMessages.CAMPAIGN_NAME_ALREADY_EXISTS);
    }

    Campaign campaign = new Campaign(campaignDto);
    campaign.setCompany(company);
    campaignRepository.save(campaign);

    log.info("Campaign with name '{}' saved successfully.", campaignDto.getUrlFriendlyName());
  }

  public CampaignDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto) {
    Campaign campaign = findByUrlFriendlyNameOrThrowException(campaignName);
    modelMapperBlankString.map(campaignPatchDto, campaign);
    return new CampaignDto(campaignRepository.save(campaign));
  }
}
