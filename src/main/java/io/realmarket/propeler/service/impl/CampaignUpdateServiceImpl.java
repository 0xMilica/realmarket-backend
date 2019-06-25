package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignUpdateDto;
import io.realmarket.propeler.api.dto.CampaignUpdateResponseDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignUpdate;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.repository.CampaignUpdateRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignUpdateImageService;
import io.realmarket.propeler.service.CampaignUpdateService;
import io.realmarket.propeler.service.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.CAMPAIGN_UPDATE_NOT_FOUND;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;

@Service
public class CampaignUpdateServiceImpl implements CampaignUpdateService {

  private final CampaignUpdateRepository campaignUpdateRepository;
  private final CampaignService campaignService;
  private final CampaignUpdateImageService campaignUpdateImageService;

  @Autowired
  public CampaignUpdateServiceImpl(
      CampaignUpdateRepository campaignUpdateRepository,
      CampaignService campaignService,
      CampaignUpdateImageService campaignUpdateImageService) {
    this.campaignUpdateRepository = campaignUpdateRepository;
    this.campaignService = campaignService;
    this.campaignUpdateImageService = campaignUpdateImageService;
  }

  @Override
  public CampaignUpdate findByIdOrThrowException(Long id) {
    return campaignUpdateRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_UPDATE_NOT_FOUND));
  }

  @Override
  public Page<CampaignUpdate> findCampaignUpdates(Auth auth, Pageable pageable) {
    return campaignUpdateRepository.findCampaignUpdates(auth, pageable);
  }

  @Override
  public Page<CampaignUpdate> findCampaignUpdatesByCampaignState(
      Auth auth, String campaignState, Pageable pageable) {
    return campaignUpdateRepository.findCampaignUpdatesByCampaignState(
        auth, CampaignStateName.valueOf(campaignState.toUpperCase()), pageable);
  }

  @Override
  public CampaignUpdateResponseDto createCampaignUpdate(
      String campaignName, CampaignUpdateDto campaignUpdateDto) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);

    campaignService.throwIfNoAccess(campaign);
    campaignService.throwIfNotActive(campaign);

    return new CampaignUpdateResponseDto(
        campaignUpdateRepository.save(
            CampaignUpdate.builder()
                .campaign(campaign)
                .title(campaignUpdateDto.getTitle())
                .content(campaignUpdateDto.getContent())
                .postDate(Instant.now())
                .build()));
  }

  @Override
  public CampaignUpdateResponseDto updateCampaignUpdate(
      Long id, CampaignUpdateDto campaignUpdateDto) {
    CampaignUpdate campaignUpdate = findByIdOrThrowException(id);

    campaignService.throwIfNoAccess(campaignUpdate.getCampaign());
    campaignService.throwIfNotActive(campaignUpdate.getCampaign());

    campaignUpdate.setContent(campaignUpdateDto.getContent());
    campaignUpdate = campaignUpdateRepository.save(campaignUpdate);

    campaignUpdateImageService.removeObsoleteImages(campaignUpdate);

    return new CampaignUpdateResponseDto(campaignUpdate);
  }

  @Override
  public CampaignUpdateResponseDto getCampaignUpdate(Long id) {
    return new CampaignUpdateResponseDto(findByIdOrThrowException(id));
  }

  @Override
  public Page<CampaignUpdateResponseDto> getCampaignUpdates(Pageable pageable, String filter) {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    if (filter.equalsIgnoreCase("all")) {
      return findCampaignUpdates(auth, pageable).map(CampaignUpdateResponseDto::new);
    } else if (filter.equalsIgnoreCase("active") || filter.equalsIgnoreCase("post_campaign")) {
      return findCampaignUpdatesByCampaignState(auth, filter, pageable)
          .map(CampaignUpdateResponseDto::new);
    }
    throw new BadRequestException(INVALID_REQUEST);
  }

  @Override
  public Page<CampaignUpdateResponseDto> getCampaignUpdatesForCampaign(
      String campaignName, Pageable pageable) {
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignName);
    return campaignUpdateRepository
        .findByCampaign(campaign, pageable)
        .map(CampaignUpdateResponseDto::new);
  }
}
