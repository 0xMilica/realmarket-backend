package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.service.CampaignStateService;
import io.realmarket.propeler.service.impl.CampaignStateServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.unit.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.unit.util.AuthUtils.mockRequestAndContextEntrepreneur;
import static io.realmarket.propeler.unit.util.CampaignUtils.TEST_CAMPAIGN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign, new CampaignState(100L, CampaignStateName.REVIEW_READY.toString()), true);

    assertTrue(isValid);
  }

  @Test
  public void changeState_InvalidState() {
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign, new CampaignState(100L, CampaignStateName.ACTIVE.toString()), true);

    assertFalse(isValid);
  }

  @Test
  public void changeState_RoleEntrepreneurButNotOwner() {
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();

    boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign,
            new CampaignState(100L, CampaignStateName.REVIEW_READY.toString()),
            false);

    assertFalse(isValid);
  }

  @Test
  public void changeState_RoleHasNoPermission() {
    mockRequestAndContext();
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();
    boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign,
            new CampaignState(100L, CampaignStateName.REVIEW_READY.toString()),
            false);

    assertFalse(isValid);
  }

  @Test
  public void changeState_InvalidStateAndRoleHasNoPermission() {
    mockRequestAndContext();
    Campaign testCampaign = TEST_CAMPAIGN.toBuilder().build();

    boolean isValid =
        campaignStateServiceImpl.changeState(
            testCampaign, new CampaignState(100L, CampaignStateName.ACTIVE.toString()), false);

    assertFalse(isValid);
  }
}
