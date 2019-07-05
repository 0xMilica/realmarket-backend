package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.service.CampaignStateService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContextEntrepreneur;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignStateService.class)
public class CampaignStateServiceImplTest {

  @InjectMocks private CampaignStateServiceImpl campaignStateServiceImpl;

  @Before
  public void createAuthContext() {
    mockRequestAndContextEntrepreneur();
  }

  @Test
  public void ifStateCanBeChanged_Success() {
    boolean isValid =
        campaignStateServiceImpl.ifStateCanBeChanged(
            CampaignState.builder().name(CampaignStateName.INITIAL).build(),
            CampaignState.builder().name(CampaignStateName.REVIEW_READY).build());

    assertTrue(isValid);
  }

  @Test
  public void ifStateCanBeChanged_Invalid_State() {
    boolean isValid =
        campaignStateServiceImpl.ifStateCanBeChanged(
            CampaignState.builder().name(CampaignStateName.INITIAL).build(),
            CampaignState.builder().name(CampaignStateName.ACTIVE).build());

    assertFalse(isValid);
  }

  @Test
  public void ifStateCanBeChanged_Role_Has_No_Permission() {
    mockRequestAndContext();
    boolean isValid =
        campaignStateServiceImpl.ifStateCanBeChanged(
            CampaignState.builder().name(CampaignStateName.INITIAL).build(),
            CampaignState.builder().name(CampaignStateName.REVIEW_READY).build());

    assertFalse(isValid);
  }

  @Test
  public void ifStateCanBeChanged_Invalid_State_And_Role_Has_No_Permission() {
    mockRequestAndContext();
    boolean isValid =
        campaignStateServiceImpl.ifStateCanBeChanged(
            CampaignState.builder().name(CampaignStateName.INITIAL).build(),
            CampaignState.builder().name(CampaignStateName.ACTIVE).build());

    assertFalse(isValid);
  }

  @Test
  public void changeStateOrThrow_Should_Change_State() {

    Campaign campaign = CampaignUtils.TEST_CAMPAIGN.toBuilder().build();
    campaignStateServiceImpl.changeStateOrThrow(
        campaign, CampaignUtils.TEST_REVIEW_READY_CAMPAIGN_STATE);

    assertEquals(CampaignUtils.TEST_REVIEW_READY_CAMPAIGN_STATE, campaign.getCampaignState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void changeStateOrThrow_Should_Throw_When_Role_Has_No_Permission() {

    Campaign campaign = CampaignUtils.TEST_REVIEW_READY_CAMPAIGN.toBuilder().build();
    campaignStateServiceImpl.changeStateOrThrow(campaign, CampaignUtils.TEST_CAMPAIGN_ACTIVE_STATE);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void changeStateOrThrow_Should_Throw_When_Invalid_State() {

    Campaign campaign = CampaignUtils.TEST_CAMPAIGN.toBuilder().build();
    campaignStateServiceImpl.changeStateOrThrow(campaign, CampaignUtils.TEST_CAMPAIGN_ACTIVE_STATE);
  }
}
