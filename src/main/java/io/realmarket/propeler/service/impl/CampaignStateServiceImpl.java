package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.repository.CampaignStateRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.CAMPAIGN_STATE_NOT_FOUND;

@Service
public class CampaignStateServiceImpl implements CampaignStateService {

  private Map<CampaignStateName, List<CampaignStateName>> stateTransitFlow =
      createAndInitStateChangeFlow();
  private Map<CampaignStateName, List<EUserRole>> rolesPerState = createAndInitRolesPerState();

  private final CampaignStateRepository campaignStateRepository;

  @Autowired
  public CampaignStateServiceImpl(CampaignStateRepository campaignStateRepository) {
    this.campaignStateRepository = campaignStateRepository;
  }

  @Override
  public boolean changeState(
      Campaign campaign, CampaignState followingCampaignState, boolean isOwner) {
    CampaignState currentCampaignState = campaign.getCampaignState();
    EUserRole eUserRole = AuthenticationUtil.getAuthentication().getAuth().getUserRole().getName();

    if (eUserRole.equals(EUserRole.ROLE_ENTREPRENEUR) && !isOwner) {
      return false;
    }

    if (stateTransitFlow
            .get(currentCampaignState.getName())
            .contains(followingCampaignState.getName())
        && rolesPerState.get(currentCampaignState.getName()).contains(eUserRole)) {
      campaign.setCampaignState(followingCampaignState);
      return true;
    }
    return false;
  }

  @Override
  public boolean hasReadAccess() {
    return false;
  }

  @Override
  public boolean hasWriteAccess() {
    return false;
  }

  @Override
  public CampaignState getCampaignStateByName(String name) {
    return campaignStateRepository
        .findByName(CampaignStateName.fromString(name))
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
        Arrays.asList(CampaignStateName.INITIAL, CampaignStateName.FINANCE_PROPOSITION));
    campaignStateTransitFlow.put(
        CampaignStateName.FINANCE_PROPOSITION,
        Arrays.asList(CampaignStateName.INITIAL, CampaignStateName.LEAD_INVESTMENT));
    campaignStateTransitFlow.put(
        CampaignStateName.LEAD_INVESTMENT,
        Arrays.asList(CampaignStateName.INITIAL, CampaignStateName.ACTIVE));
    campaignStateTransitFlow.put(
        CampaignStateName.ACTIVE, Collections.singletonList(CampaignStateName.POST_CAMPAIGN));
    campaignStateTransitFlow.put(CampaignStateName.POST_CAMPAIGN, Collections.emptyList());
    return campaignStateTransitFlow;
  }

  private Map<CampaignStateName, List<EUserRole>> createAndInitRolesPerState() {
    Map<CampaignStateName, List<EUserRole>> rolesPerCampaignState =
        new EnumMap<>(CampaignStateName.class);
    rolesPerCampaignState.put(
        CampaignStateName.INITIAL, Collections.singletonList(EUserRole.ROLE_ENTREPRENEUR));
    rolesPerCampaignState.put(
        CampaignStateName.REVIEW_READY, Collections.singletonList(EUserRole.ROLE_AUDITOR));
    rolesPerCampaignState.put(
        CampaignStateName.AUDIT, Collections.singletonList(EUserRole.ROLE_AUDITOR));
    rolesPerCampaignState.put(
        CampaignStateName.FINANCE_PROPOSITION,
        Collections.singletonList(EUserRole.ROLE_ENTREPRENEUR));
    rolesPerCampaignState.put(
        CampaignStateName.LEAD_INVESTMENT, Collections.singletonList(EUserRole.ROLE_ADMIN));
    rolesPerCampaignState.put(
        CampaignStateName.ACTIVE, Collections.singletonList(EUserRole.ROLE_ADMIN));
    rolesPerCampaignState.put(CampaignStateName.POST_CAMPAIGN, Collections.emptyList());
    return rolesPerCampaignState;
  }
}
