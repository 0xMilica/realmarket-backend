package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CampaignStateService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CampaignStateServiceImpl implements CampaignStateService {

  private final CampaignService campaignService;

  public CampaignStateServiceImpl(CampaignService campaignService) {
    this.campaignService = campaignService;
  }

  private Map<CampaignStateName, List<CampaignStateName>> stateTransitFlow =
      createAndInitStateChangeFlow();
  private Map<CampaignState, List<EUserRole>> rolesPerState = createAndInitRolesPerState();

  @Override
  public void changeState(String campaignUrlFriendlyName, CampaignState followingCampaignState) {
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignUrlFriendlyName);
    CampaignState currentCampaignState = campaign.getCampaignState();
    EUserRole eUserRole = AuthenticationUtil.getAuthentication().getAuth().getUserRole();

    if (stateTransitFlow
            .get(CampaignStateName.fromString(currentCampaignState.getName()))
            .toString()
            .equals(followingCampaignState.getName())
        && rolesPerState.get(currentCampaignState).contains(eUserRole)) {
      campaign.setCampaignState(followingCampaignState);
    }
  }

  @Override
  public boolean hasReadAccess() {
    return false;
  }

  @Override
  public boolean hasWriteAccess() {
    return false;
  }

  private Map createAndInitStateChangeFlow() {
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

  private Map createAndInitRolesPerState() {
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
