package io.realmarket.propeler.service.impl;

import com.google.common.base.MoreObjects;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.ShareholderDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Shareholder;
import io.realmarket.propeler.repository.ShareholderRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.ShareholderService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.FileUtils;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
@Slf4j
public class ShareholderServiceImpl implements ShareholderService {
  private ShareholderRepository shareholderRepository;
  private CampaignService campaignService;
  private ModelMapperBlankString modelMapperBlankString;
  private CloudObjectStorageService cloudObjectStorageService;

  @Value(value = "${cos.file_prefix.shareholder_picture}")
  private String shareholderPicturePrefix;

  @Autowired
  ShareholderServiceImpl(
      ShareholderRepository shareholderRepository,
      CampaignService campaignService,
      ModelMapperBlankString modelMapperBlankString,
      CloudObjectStorageService cloudObjectStorageService) {
    this.shareholderRepository = shareholderRepository;
    this.campaignService = campaignService;
    this.modelMapperBlankString = modelMapperBlankString;
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  private void throwIfNoAccess(Shareholder shareholder, String campaignName) {
    Campaign campaign = shareholder.getCampaign();
    if (!campaignName.equals(campaign.getUrlFriendlyName())) {
      throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_CAMPAIGN);
    }
    campaignService.throwIfNoAccess(shareholder.getCampaign());
  }

  public Shareholder findByIdOrThrowException(Long shareholderId) {
    return shareholderRepository
        .findById(shareholderId)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_INVESTOR_NOT_FOUND));
  }

  @Transactional
  @Override
  public Shareholder createShareholder(String campaignName, ShareholderDto shareholderDto) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNoAccess(campaign);
    Shareholder shareholder = shareholderDto.createShareholder(campaign);
    Integer order = MoreObjects.firstNonNull(shareholderRepository.getMaxOrder(campaignName), 0);
    shareholder.setOrderNumber(++order);
    return shareholderRepository.save(shareholder);
  }

  @Transactional
  public List<Shareholder> patchShareholderOrder(String campaignName, List<Long> order) {
    order.forEach(
        memberId -> {
          Shareholder shareholder = findByIdOrThrowException(memberId);
          if (!campaignName.equals(shareholder.getCampaign().getUrlFriendlyName())) {
            throw new IllegalArgumentException(INVALID_REQUEST);
          }
          shareholder.setOrderNumber(order.indexOf(memberId));
          shareholderRepository.save(shareholder);
        });
    log.info("Saving new order of shareholders!");
    List<Shareholder> shareholders = getShareholders(campaignName);
    if (shareholders.size() != order.size()) {
      throw new IllegalArgumentException(INVALID_REQUEST);
    }
    return shareholders;
  }

  @Override
  public List<Shareholder> getShareholders(String campaignName) {
    return shareholderRepository.findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(
        campaignName);
  }

  @Override
  public Shareholder patchShareholder(
      String campaignName, Long shareholderId, ShareholderDto shareholderDto) {
    Shareholder shareholder = findByIdOrThrowException(shareholderId);
    throwIfNoAccess(shareholder, campaignName);
    modelMapperBlankString.map(shareholderDto, shareholder);
    return shareholderRepository.save(shareholder);
  }

  @Override
  public void deleteShareholder(String campaignName, Long shareholderId) {
    Shareholder shareholder = findByIdOrThrowException(shareholderId);
    throwIfNoAccess(shareholder, campaignName);
    shareholderRepository.delete(shareholder);
  }

  @Override
  public void uploadPicture(String campaignName, Long shareholderId, MultipartFile picture) {
    Shareholder shareholder = findByIdOrThrowException(shareholderId);
    throwIfNoAccess(shareholder, campaignName);
    String extension = FileUtils.getExtensionOrThrowException(picture);
    String url =
        String.join("", shareholderPicturePrefix, shareholder.getId().toString(), ".", extension);
    cloudObjectStorageService.uploadAndReplace(shareholder.getPhotoUrl(), url, picture);
    shareholder.setPhotoUrl(url);
    shareholderRepository.save(shareholder);
  }

  @Override
  public FileDto downloadPicture(String campaignName, Long shareholderId) {
    return cloudObjectStorageService.downloadFileDto(
        findByIdOrThrowException(shareholderId).getPhotoUrl());
  }

  @Override
  public void deletePicture(String campaignName, Long shareholderId) {
    log.info("Delete shareholder[{}] picture requested", shareholderId);
    Shareholder shareholder = findByIdOrThrowException(shareholderId);
    throwIfNoAccess(shareholder, campaignName);
    cloudObjectStorageService.delete(shareholder.getPhotoUrl());
    shareholder.setPhotoUrl(null);
    shareholderRepository.save(shareholder);
  }
}
