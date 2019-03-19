package io.realmarket.propeler.service.impl;

import com.google.common.base.MoreObjects;
import io.realmarket.propeler.api.dto.CampaignInvestorDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignInvestor;
import io.realmarket.propeler.repository.CampaignInvestorRepository;
import io.realmarket.propeler.service.CampaignInvestorService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CloudObjectStorageService;
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
public class CampaignInvestorServiceImpl implements CampaignInvestorService {
  private CampaignInvestorRepository campaignInvestorRepository;
  private CampaignService campaignService;
  private ModelMapperBlankString modelMapperBlankString;
  private CloudObjectStorageService cloudObjectStorageService;

  @Value(value = "${cos.file_prefix.campaign_investor_picture}")
  private String campaignInvestorPicturePrefix;

  @Autowired
  CampaignInvestorServiceImpl(
      CampaignInvestorRepository campaignInvestorRepository,
      CampaignService campaignService,
      ModelMapperBlankString modelMapperBlankString,
      CloudObjectStorageService cloudObjectStorageService) {
    this.campaignInvestorRepository = campaignInvestorRepository;
    this.campaignService = campaignService;
    this.modelMapperBlankString = modelMapperBlankString;
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  private void throwIfNoAccess(CampaignInvestor campaignInvestor, String campaignName) {
    Campaign campaign = campaignInvestor.getCampaign();
    if (!campaignName.equals(campaign.getUrlFriendlyName())) {
      throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_CAMPAIGN);
    }
    campaignService.throwIfNoAccess(campaignInvestor.getCampaign());
  }

  public CampaignInvestor findByIdOrThrowException(Long investorId) {
    return campaignInvestorRepository
        .findById(investorId)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_INVESTOR_NOT_FOUND));
  }

  @Transactional
  @Override
  public CampaignInvestor createCampaignInvestor(
      String campaignName, CampaignInvestorDto campaignInvestorDto) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNoAccess(campaign);
    CampaignInvestor campaignInvestor = campaignInvestorDto.createInvestor(campaign);
    Integer order =
        MoreObjects.firstNonNull(campaignInvestorRepository.getMaxOrder(campaignName), 0);
    campaignInvestor.setOrderNumber(++order);
    return campaignInvestorRepository.save(campaignInvestor);
  }

  @Transactional
  public List<CampaignInvestor> patchCampaignInvestorOrder(String campaignName, List<Long> order) {
    order.forEach(
        memberId -> {
          CampaignInvestor campaignInvestor = findByIdOrThrowException(memberId);
          if (!campaignName.equals(campaignInvestor.getCampaign().getUrlFriendlyName())) {
            throw new IllegalArgumentException(INVALID_REQUEST);
          }
          campaignInvestor.setOrderNumber(order.indexOf(memberId));
          campaignInvestorRepository.save(campaignInvestor);
        });
    log.info("Saving new order of campaign investors!");
    List<CampaignInvestor> campaignInvestors = getCampaignInvestors(campaignName);
    if (campaignInvestors.size() != order.size()) {
      throw new IllegalArgumentException(INVALID_REQUEST);
    }
    return campaignInvestors;
  }

  @Override
  public List<CampaignInvestor> getCampaignInvestors(String campaignName) {
    return campaignInvestorRepository.findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(
        campaignName);
  }

  @Override
  public CampaignInvestor patchCampaignInvestor(
      String campaignName, Long investorId, CampaignInvestorDto campaignInvestorDto) {
    CampaignInvestor campaignInvestor = findByIdOrThrowException(investorId);
    throwIfNoAccess(campaignInvestor, campaignName);
    modelMapperBlankString.map(campaignInvestorDto, campaignInvestor);
    return campaignInvestorRepository.save(campaignInvestor);
  }

  @Override
  public void deleteCampaignInvestor(String campaignName, Long investorId) {
    CampaignInvestor campaignInvestor = findByIdOrThrowException(investorId);
    throwIfNoAccess(campaignInvestor, campaignName);
    campaignInvestorRepository.delete(campaignInvestor);
  }

  @Override
  public void uploadPicture(String campaignName, Long investorId, MultipartFile picture) {
    CampaignInvestor campaignInvestor = findByIdOrThrowException(investorId);
    throwIfNoAccess(campaignInvestor, campaignName);
    String extension = FileUtils.getExtensionOrThrowException(picture);
    String url =
        String.join(
            "", campaignInvestorPicturePrefix, campaignInvestor.getId().toString(), ".", extension);
    cloudObjectStorageService.uploadAndReplace(campaignInvestor.getPhotoUrl(), url, picture);
    campaignInvestor.setPhotoUrl(url);
    campaignInvestorRepository.save(campaignInvestor);
  }

  @Override
  public FileDto downloadPicture(String campaignName, Long investorId) {
    return cloudObjectStorageService.downloadFileDto(
        findByIdOrThrowException(investorId).getPhotoUrl());
  }

  @Override
  public void deletePicture(String campaignName, Long investorId) {
    log.info("Delete investor[{}] picture requested", investorId);
    CampaignInvestor campaignInvestor = findByIdOrThrowException(investorId);
    throwIfNoAccess(campaignInvestor, campaignName);
    cloudObjectStorageService.delete(campaignInvestor.getPhotoUrl());
    campaignInvestor.setPhotoUrl(null);
    campaignInvestorRepository.save(campaignInvestor);
  }
}
