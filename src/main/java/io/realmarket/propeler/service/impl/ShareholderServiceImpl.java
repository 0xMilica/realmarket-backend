package io.realmarket.propeler.service.impl;

import com.google.common.base.MoreObjects;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.ShareholderRequestDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Shareholder;
import io.realmarket.propeler.repository.ShareholderRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.PlatformSettingsService;
import io.realmarket.propeler.service.ShareholderService;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.company.UpdateShareholdersDto;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
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

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.SHAREHOLDER_NOT_FOUND;

@Service
@Slf4j
public class ShareholderServiceImpl implements ShareholderService {
  private ShareholderRepository shareholderRepository;
  private CompanyService companyService;
  private PlatformSettingsService platformSettingsService;
  private ModelMapperBlankString modelMapperBlankString;
  private CloudObjectStorageService cloudObjectStorageService;
  private BlockchainCommunicationService blockchainCommunicationService;

  @Value(value = "${cos.file_prefix.shareholder_picture}")
  private String shareholderPicturePrefix;

  @Autowired
  ShareholderServiceImpl(
      ShareholderRepository shareholderRepository,
      CompanyService companyService,
      PlatformSettingsService platformSettingsService,
      ModelMapperBlankString modelMapperBlankString,
      CloudObjectStorageService cloudObjectStorageService,
      BlockchainCommunicationService blockchainCommunicationService) {
    this.shareholderRepository = shareholderRepository;
    this.companyService = companyService;
    this.platformSettingsService = platformSettingsService;
    this.modelMapperBlankString = modelMapperBlankString;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.blockchainCommunicationService = blockchainCommunicationService;
  }

  public Shareholder findByIdOrThrowException(Long shareholderId) {
    return shareholderRepository
        .findById(shareholderId)
        .orElseThrow(() -> new EntityNotFoundException(SHAREHOLDER_NOT_FOUND));
  }

  @Transactional
  @Override
  public Shareholder createShareholder(ShareholderRequestDto shareholderRequestDto) {
    Company company = companyService.findMyCompany();
    Shareholder shareholder = shareholderRequestDto.createShareholder(company);
    shareholder.setCurrency(platformSettingsService.getPlatformCurrency().getCode());
    Integer order = MoreObjects.firstNonNull(shareholderRepository.getMaxOrder(company), 0);
    shareholder.setOrderNumber(++order);
    shareholder = shareholderRepository.save(shareholder);

    blockchainCommunicationService.invoke(
        BlockchainMethod.SUBMIT_SHAREHOLDERS,
        new UpdateShareholdersDto(company, getShareholders()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return shareholder;
  }

  @Transactional
  public List<Shareholder> patchShareholderOrder(List<Long> order) {
    Company company = companyService.findMyCompany();
    order.forEach(
        memberId -> {
          Shareholder shareholder = findByIdOrThrowException(memberId);
          if (!company.equals(shareholder.getCompany())) {
            throw new IllegalArgumentException(INVALID_REQUEST);
          }
          shareholder.setOrderNumber(order.indexOf(memberId));
          shareholderRepository.save(shareholder);
        });
    log.info("Saving new order of shareholders!");
    List<Shareholder> shareholders = getShareholders(company.getId());
    if (shareholders.size() != order.size()) {
      throw new IllegalArgumentException(INVALID_REQUEST);
    }
    return shareholders;
  }

  @Override
  public List<Shareholder> getShareholders(Long companyId) {
    return shareholderRepository.findAllByCompanyIdOrderByOrderNumberAsc(companyId);
  }

  @Override
  public List<Shareholder> getShareholders() {
    Company company = companyService.findMyCompany();
    return shareholderRepository.findAllByCompanyIdOrderByOrderNumberAsc(company.getId());
  }

  @Override
  public Shareholder patchShareholder(
      Long shareholderId, ShareholderRequestDto shareholderRequestDto) {
    Shareholder shareholder = findByIdOrThrowException(shareholderId);
    companyService.throwIfNotCompanyOwner();
    modelMapperBlankString.map(shareholderRequestDto, shareholder);
    shareholder = shareholderRepository.save(shareholder);

    blockchainCommunicationService.invoke(
        BlockchainMethod.SUBMIT_SHAREHOLDERS,
        new UpdateShareholdersDto(shareholder.getCompany(), getShareholders()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return shareholder;
  }

  @Override
  public void deleteShareholder(Long shareholderId) {
    Shareholder shareholder = findByIdOrThrowException(shareholderId);
    companyService.throwIfNotCompanyOwner();
    shareholderRepository.delete(shareholder);

    blockchainCommunicationService.invoke(
        BlockchainMethod.SUBMIT_SHAREHOLDERS,
        new UpdateShareholdersDto(shareholder.getCompany(), getShareholders()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());
  }

  @Override
  public void uploadPicture(Long shareholderId, MultipartFile picture) {
    Shareholder shareholder = findByIdOrThrowException(shareholderId);
    companyService.throwIfNotCompanyOwner();
    String extension = FileUtils.getExtensionOrThrowException(picture);
    String url =
        String.join("", shareholderPicturePrefix, shareholder.getId().toString(), ".", extension);
    cloudObjectStorageService.uploadAndReplace(shareholder.getPhotoUrl(), url, picture);
    shareholder.setPhotoUrl(url);
    shareholderRepository.save(shareholder);
  }

  @Override
  public FileDto downloadPicture(Long shareholderId) {
    Auth user = AuthenticationUtil.getAuthentication().getAuth();
    switch (user.getUserRole().getName()) {
      case ROLE_ADMIN:
        break;
      case ROLE_ENTREPRENEUR:
        companyService.throwIfNotCompanyOwner();
        break;
      default:
        throw new EntityNotFoundException(ExceptionMessages.PROFILE_PICTURE_DOES_NOT_EXIST);
    }
    return cloudObjectStorageService.downloadFileDto(
        findByIdOrThrowException(shareholderId).getPhotoUrl());
  }

  @Override
  public FileDto downloadPublicPicture(Long companyId, Long shareholderId) {
    Shareholder shareholder = shareholderRepository.getByIdAndCompanyId(shareholderId, companyId);
    if (shareholder.getIsAnonymous()) {
      throw new EntityNotFoundException(ExceptionMessages.PROFILE_PICTURE_DOES_NOT_EXIST);
    }
    return cloudObjectStorageService.downloadFileDto(
        findByIdOrThrowException(shareholderId).getPhotoUrl());
  }

  @Override
  public void deletePicture(Long shareholderId) {
    log.info("Delete shareholder[{}] picture requested", shareholderId);
    Shareholder shareholder = findByIdOrThrowException(shareholderId);
    companyService.throwIfNotCompanyOwner();
    cloudObjectStorageService.delete(shareholder.getPhotoUrl());
    shareholder.setPhotoUrl(null);
    shareholderRepository.save(shareholder);
  }
}
