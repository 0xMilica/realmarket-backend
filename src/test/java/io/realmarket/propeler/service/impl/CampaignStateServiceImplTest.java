package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.service.CampaignStateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.unit.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.unit.util.AuthUtils.mockRequestAndContextEntrepreneur;
import static io.realmarket.propeler.unit.util.CampaignUtils.TEST_CAMPAIGN;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignStateService.class)
public class CampaignStateServiceImplTest {

  @InjectMocks private CampaignStateServiceImpl campaignStateServiceImpl;

  @Before
  public void createAuthContext() {
    mockRequestAndContextEntrepreneur();
  }

  @Test
  public void changeState_Success() {
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();

    Boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign, new CampaignState(100L, CampaignStateName.REVIEW_READY));

    assertEquals(true, isValid);
  }

  @Test
  public void changeState_InvalidState() {
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    Boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign, new CampaignState(100L, CampaignStateName.ACTIVE));

    assertEquals(false, isValid);
  }

  public void createUserAuthContext() {
    mockRequestAndContext();
  }

  @Test
  public void changeState_RoleHasNoPermission() {
    createUserAuthContext();
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    Boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign, new CampaignState(100L, CampaignStateName.REVIEW_READY));

    assertEquals(false, isValid);
  }

  @Test
  public void changeState_InvalidStateAndRoleHasNoPermission() {
    createUserAuthContext();
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();

    Boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign, new CampaignState(100L, CampaignStateName.ACTIVE));

    assertEquals(false, isValid);
  }
}
