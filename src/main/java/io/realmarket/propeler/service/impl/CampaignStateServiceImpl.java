package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.repository.CampaignStateRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignStateService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.CAMPAIGN_STATE_NOT_FOUND;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.FORBIDDEN_OPERATION_EXCEPTION;

@Service
public class CampaignStateServiceImpl implements CampaignStateService {

  private final CampaignStateRepository campaignStateRepository;
  private final CampaignRepository campaignRepository;

  private Map<CampaignStateName, List<CampaignStateName>> stateTransitFlow =
      createAndInitStateChangeFlow();
  private Map<CampaignStateName, List<UserRoleName>> rolesPerState = createAndInitRolesPerState();

  @Autowired
  public CampaignStateServiceImpl(
      CampaignStateRepository campaignStateRepository, CampaignRepository campaignRepository) {
    this.campaignStateRepository = campaignStateRepository;
    this.campaignRepository = campaignRepository;
  }

  @Override
  public void changeState(Campaign campaign, CampaignState followingCampaignState) {
    CampaignState currentCampaignState = campaign.getCampaignState();
    UserRoleName userRoleName =
        AuthenticationUtil.getAuthentication().getAuth().getUserRole().getName();

    if (stateTransitFlow
            .get(currentCampaignState.getName())
            .contains(followingCampaignState.getName())
        && rolesPerState.get(currentCampaignState.getName()).contains(userRoleName)) {
      campaign.setCampaignState(followingCampaignState);
      campaignRepository.save(campaign);
    } else throw new ForbiddenOperationException(FORBIDDEN_OPERATION_EXCEPTION);
  }

  @Override
  public CampaignState getCampaignState(String name) {
    return campaignStateRepository
        .findByName(CampaignStateName.fromString(name))
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_STATE_NOT_FOUND));
  }

  @Override
  public CampaignState getCampaignState(CampaignStateName campaignStateName) {
    return campaignStateRepository
        .findByName(campaignStateName)
        .orElseThrow(() -> new EntityNotFoundException(CAMPAIGN_STATE_NOT_FOUND));
  }

  private Map<CampaignStateName, List<CampaignStateName>> createAndInitStateChangeFlow() {
    Map<CampaignStateName, List<CampaignStateName>> campaignStateTransitFlow =
        new EnumMap<>(CampaignStateName.class);
    campaignStateTransitFlow.put(
        CampaignStateName.INITIAL, Collections.singletonList(CampaignStateName.REVIEW_READY));
    campaignStateTransitFlow.put(
        CampaignStateName.REVIEW_READY, Collections.singletonList(CampaignStateName.AUDIT));
    campaignStateTransitFlow.put(
        CampaignStateName.AUDIT,
        Arrays.asList(CampaignStateName.INITIAL, CampaignStateName.LAUNCH_READY));
    campaignStateTransitFlow.put(
        CampaignStateName.LAUNCH_READY, Collections.singletonList(CampaignStateName.ACTIVE));
    campaignStateTransitFlow.put(
        CampaignStateName.ACTIVE, Collections.singletonList(CampaignStateName.POST_CAMPAIGN));
    campaignStateTransitFlow.put(CampaignStateName.POST_CAMPAIGN, Collections.emptyList());
    return campaignStateTransitFlow;
  }

  private Map<CampaignStateName, List<UserRoleName>> createAndInitRolesPerState() {
    Map<CampaignStateName, List<UserRoleName>> rolesPerCampaignState =
        new EnumMap<>(CampaignStateName.class);
    rolesPerCampaignState.put(
        CampaignStateName.INITIAL, Collections.singletonList(UserRoleName.ROLE_ENTREPRENEUR));
    rolesPerCampaignState.put(
        CampaignStateName.REVIEW_READY, Collections.singletonList(UserRoleName.ROLE_ENTREPRENEUR));
    rolesPerCampaignState.put(
        CampaignStateName.AUDIT, Collections.singletonList(UserRoleName.ROLE_ADMIN));
    rolesPerCampaignState.put(
        CampaignStateName.LAUNCH_READY, Collections.singletonList(UserRoleName.ROLE_ADMIN));
    rolesPerCampaignState.put(
        CampaignStateName.ACTIVE, Collections.singletonList(UserRoleName.ROLE_ADMIN));
    rolesPerCampaignState.put(CampaignStateName.POST_CAMPAIGN, Collections.emptyList());
    return rolesPerCampaignState;
  }
}
