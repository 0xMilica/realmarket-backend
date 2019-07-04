package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.repository.CampaignRepository;
import io.realmarket.propeler.service.CampaignStateService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContextEntrepreneur;
import static io.realmarket.propeler.util.CampaignUtils.TEST_CAMPAIGN;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignStateService.class)
public class CampaignStateServiceImplTest {

  @Mock private CampaignRepository campaignRepository;

  @InjectMocks private CampaignStateServiceImpl campaignStateServiceImpl;

  @Before
  public void createAuthContext() {
    mockRequestAndContextEntrepreneur();
  }

  @Test
  public void changeState_Success() {
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();

    campaignStateServiceImpl.changeState(
        testCampaign, CampaignState.builder().name(CampaignStateName.REVIEW_READY).build());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void changeState_InvalidState() {
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    campaignStateServiceImpl.changeState(
        testCampaign, CampaignState.builder().name(CampaignStateName.ACTIVE).build());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void changeState_RoleHasNoPermission() {
    mockRequestAndContext();
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    campaignStateServiceImpl.changeState(
        testCampaign, CampaignState.builder().name(CampaignStateName.REVIEW_READY).build());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void changeState_InvalidStateAndRoleHasNoPermission() {
    mockRequestAndContext();
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();

    campaignStateServiceImpl.changeState(
        testCampaign, CampaignState.builder().name(CampaignStateName.ACTIVE).build());
  }
}
