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

  private Map<CampaignStateName, List<CampaignStateName>> stateChangeFlow = createAndInitStateChangeFlow();
  private Map<CampaignState, List<EUserRole>> rolesPerState = createAndInitRolesPerState();

  @Override
  public void changeState(String campaignUrlFriendlyName, CampaignState followingCampaignState) {
    Campaign campaign = campaignService.getCampaignByUrlFriendlyName(campaignUrlFriendlyName);
    CampaignState currentCampaignState = campaign.getCampaignState();
    EUserRole eUserRole = AuthenticationUtil.getAuthentication().getAuth().getUserRole();

    if (stateChangeFlow.get(currentCampaignState).contains(followingCampaignState.getName())
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
    Map<CampaignStateName, List<CampaignStateName>> stateChangeFlow = new EnumMap<>(CampaignStateName.class);
    stateChangeFlow.put(CampaignStateName.INITIAL, Arrays.asList(CampaignStateName.REVIEW_READY));
    stateChangeFlow.put(CampaignStateName.REVIEW_READY, Arrays.asList(CampaignStateName.AUDIT));
    stateChangeFlow.put(
        CampaignStateName.AUDIT,
        Arrays.asList(CampaignStateName.INITIAL, CampaignStateName.FINANCE_PROPOSITION));
    stateChangeFlow.put(
        CampaignStateName.FINANCE_PROPOSITION,
        Arrays.asList(CampaignStateName.INITIAL, CampaignStateName.LEAD_INVESTMENT));
    stateChangeFlow.put(
        CampaignStateName.LEAD_INVESTMENT,
        Arrays.asList(CampaignStateName.INITIAL, CampaignStateName.ACTIVE));
    stateChangeFlow.put(CampaignStateName.ACTIVE, Arrays.asList(CampaignStateName.POST_CAMPAIGN));
    stateChangeFlow.put(CampaignStateName.POST_CAMPAIGN, Arrays.asList());
    return stateChangeFlow;
  }

  private Map createAndInitRolesPerState() {
    Map<CampaignStateName, List<EUserRole>> rolesPerState = new EnumMap<>(CampaignStateName.class);
    rolesPerState.put(CampaignStateName.INITIAL, Arrays.asList(EUserRole.ROLE_ENTREPRENEUR));
    rolesPerState.put(CampaignStateName.REVIEW_READY, Arrays.asList(EUserRole.ROLE_AUDITOR));
    rolesPerState.put(CampaignStateName.AUDIT, Arrays.asList(EUserRole.ROLE_AUDITOR));
    rolesPerState.put(
        CampaignStateName.FINANCE_PROPOSITION, Arrays.asList(EUserRole.ROLE_ENTREPRENEUR));
    rolesPerState.put(CampaignStateName.LEAD_INVESTMENT, Arrays.asList(EUserRole.ROLE_ADMIN));
    rolesPerState.put(CampaignStateName.ACTIVE, Arrays.asList(EUserRole.ROLE_ADMIN));
    rolesPerState.put(CampaignStateName.POST_CAMPAIGN, Arrays.asList());
    return rolesPerState;
  }
}
