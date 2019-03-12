package io.realmarket.propeler.service.impl;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
import io.realmarket.propeler.api.dto.CampaignInvestorDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignInvestor;
import io.realmarket.propeler.repository.CampaignInvestorRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignInvestorService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.CAMPAIGN_INVESTOR_NOT_FOUND;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.USER_IS_NOT_OWNER_OF_CAMPAIGN;

@Service
@Slf4j
public class CampaignInvestorServiceImpl implements CampaignInvestorService {
  private CampaignInvestorRepository campaignInvestorRepository;
  private CampaignService campaignService;
  private ModelMapperBlankString modelMapperBlankString;

  @Autowired
  CampaignInvestorServiceImpl(
      CampaignInvestorRepository campaignInvestorRepository,
      CampaignService campaignService,
      ModelMapperBlankString modelMapperBlankString) {
    this.campaignInvestorRepository = campaignInvestorRepository;
    this.campaignService = campaignService;
    this.modelMapperBlankString = modelMapperBlankString;
  }

  private void throwIfNoAccess(Campaign campaign, String campaignName) {
    if (!campaign
            .getCompany()
            .getAuth()
            .getId()
            .equals(AuthenticationUtil.getAuthentication().getAuth().getId())
        || !campaign.getUrlFriendlyName().equals(campaignName)) {
      throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_CAMPAIGN);
    }
  }

  private void throwIfNoAccess(CampaignInvestor campaignInvestor, String campaignName) {
    throwIfNoAccess(campaignInvestor.getCampaign(), campaignName);
  }

  public CampaignInvestor findByIdOrThrowException(Long investorId) {
    return campaignInvestorRepository
        .findById(investorId)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_INVESTOR_NOT_FOUND));
  }

  @Transactional
  public CampaignInvestor createCampaignInvestor(
      String campaignName, CampaignInvestorDto campaignInvestorDto) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    throwIfNoAccess(campaign, campaignName);
    CampaignInvestor campaignInvestor = campaignInvestorDto.createInvestor(campaign);
    campaignInvestor.setOrderNumber(
        campaignInvestorRepository.countByCampaignUrlFriendlyName(campaignName));
    return campaignInvestorRepository.save(campaignInvestor);
  }

  @Transactional
  public List<CampaignInvestor> patchCampaignInvestorOrder(String campaignName, List<Long> order) {
    order.forEach(
        memberId ->{
          CampaignInvestor campaignInvestor = findByIdOrThrowException(memberId);
          if(!campaignInvestor.getCampaign().getUrlFriendlyName().equals(campaignName)){
            throw new IllegalArgumentException("Wrong size of order");
          }
          campaignInvestor.setOrderNumber(order.indexOf(memberId));
          campaignInvestorRepository.save(campaignInvestor);
        });
    log.info("Saving new order of campaign investors!");
    List<CampaignInvestor> campaignInvestors = getCampaignInvestors(campaignName);
    if(campaignInvestors.size() != order.size()) {
      throw new IllegalArgumentException("Wrong size of order");
    }
    return campaignInvestors;
  }

  public List<CampaignInvestor> getCampaignInvestors(String campaignName) {
    return campaignInvestorRepository.findAllByCampaignUrlFriendlyNameOrderByOrderNumberAsc(
        campaignName);
  }

  public CampaignInvestor patchCampaignInvestor(
      String campaignName, Long investorId, CampaignInvestorDto campaignInvestorDto) {
    CampaignInvestor campaignInvestor = findByIdOrThrowException(investorId);
    throwIfNoAccess(campaignInvestor, campaignName);
    modelMapperBlankString.map(campaignInvestorDto, campaignInvestor);
    return campaignInvestorRepository.save(campaignInvestor);
  }

  public void deleteCampaignInvestor(String campaignName, Long investorId) {
    CampaignInvestor campaignInvestor = findByIdOrThrowException(investorId);
    throwIfNoAccess(campaignInvestor, campaignName);
    campaignInvestorRepository.delete(campaignInvestor);
  }
}
