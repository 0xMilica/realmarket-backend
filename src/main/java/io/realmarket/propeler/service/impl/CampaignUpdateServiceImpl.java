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
  public CampaignUpdate findByIdOrThrowException(Long campaignUpdateId) {
    return campaignUpdateRepository
        .findById(campaignUpdateId)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_UPDATE_NOT_FOUND));
  }

  @Override
  public Page<CampaignUpdate> findCampaignUpdates(Pageable pageable) {
    return campaignUpdateRepository.findCampaignUpdates(pageable);
  }

  @Override
  public Page<CampaignUpdate> findMyCampaignUpdates(Auth auth, Pageable pageable) {
    return campaignUpdateRepository.findMyCampaignUpdates(auth, pageable);
  }

  @Override
  public Page<CampaignUpdate> findCampaignUpdatesByCampaignState(
      CampaignStateName campaignState, Pageable pageable) {
    return campaignUpdateRepository.findCampaignUpdatesByCampaignState(campaignState, pageable);
  }

  @Override
  public Page<CampaignUpdate> findCampaignUpdatesByCampaign(Campaign campaign, Pageable pageable) {
    return campaignUpdateRepository.findByCampaignOrderByPostDateDesc(campaign, pageable);
  }

  @Override
  public CampaignUpdateResponseDto createCampaignUpdate(
      String campaignName, CampaignUpdateDto campaignUpdateDto) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);

    campaignService.throwIfNotOwner(campaign);
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
      Long campaignUpdateId, CampaignUpdateDto campaignUpdateDto) {
    CampaignUpdate campaignUpdate = findByIdOrThrowException(campaignUpdateId);

    campaignService.throwIfNotOwner(campaignUpdate.getCampaign());
    campaignService.throwIfNotActive(campaignUpdate.getCampaign());

    campaignUpdate.setTitle(campaignUpdateDto.getTitle());
    campaignUpdate.setContent(campaignUpdateDto.getContent());
    campaignUpdate = campaignUpdateRepository.save(campaignUpdate);

    campaignUpdateImageService.removeObsoleteImages(campaignUpdate);

    return new CampaignUpdateResponseDto(campaignUpdate);
  }

  @Override
  public void deleteCampaignUpdate(Long campaignUpdateId) {
    CampaignUpdate campaignUpdate = findByIdOrThrowException(campaignUpdateId);

    campaignService.throwIfNotOwner(campaignUpdate.getCampaign());
    campaignService.throwIfNotActive(campaignUpdate.getCampaign());

    campaignUpdateImageService.removeImages(campaignUpdate);
    campaignUpdateRepository.delete(campaignUpdate);
  }

  @Override
  public CampaignUpdateResponseDto getCampaignUpdate(Long campaignUpdateId) {
    return new CampaignUpdateResponseDto(findByIdOrThrowException(campaignUpdateId));
  }

  @Override
  public Page<CampaignUpdateResponseDto> getCampaignUpdates(Pageable pageable, String filter) {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    if (filter.equalsIgnoreCase("all")) {
      return findCampaignUpdates(pageable).map(CampaignUpdateResponseDto::new);
    } else if (filter.equalsIgnoreCase("my_campaigns")) {
      return findMyCampaignUpdates(auth, pageable).map(CampaignUpdateResponseDto::new);
    } else if (filter.equalsIgnoreCase("active") || filter.equalsIgnoreCase("post_campaign")) {
      return findCampaignUpdatesByCampaignState(
              CampaignStateName.valueOf(filter.toUpperCase()), pageable)
          .map(CampaignUpdateResponseDto::new);
    }
    throw new BadRequestException(INVALID_REQUEST);
  }

  @Override
  public Page<CampaignUpdateResponseDto> getCampaignUpdatesForCampaign(
      String campaignName, Pageable pageable) {
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignName);
    return findCampaignUpdatesByCampaign(campaign, pageable).map(CampaignUpdateResponseDto::new);
  }
}
