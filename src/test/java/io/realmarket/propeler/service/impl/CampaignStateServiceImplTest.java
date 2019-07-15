package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.repository.CampaignStateRepository;
import io.realmarket.propeler.service.CampaignStateService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContextEntrepreneur;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignStateService.class)
public class CampaignStateServiceImplTest {

  @Mock private CampaignStateRepository campaignStateRepository;

  @InjectMocks private CampaignStateServiceImpl campaignStateServiceImpl;

  @Before
  public void createAuthContext() {
    mockRequestAndContextEntrepreneur();
  }

  @Test
  public void ifStateCanBeChanged_Success() {
    boolean isValid =
        campaignStateServiceImpl.ifStateCanBeChanged(
            CampaignStateName.INITIAL, CampaignStateName.REVIEW_READY);

    assertTrue(isValid);
  }

  @Test
  public void ifStateCanBeChanged_Invalid_State() {
    boolean isValid =
        campaignStateServiceImpl.ifStateCanBeChanged(
            CampaignStateName.INITIAL, CampaignStateName.ACTIVE);

    assertFalse(isValid);
  }

  @Test
  public void ifStateCanBeChanged_Role_Has_No_Permission() {
    mockRequestAndContext();
    boolean isValid =
        campaignStateServiceImpl.ifStateCanBeChanged(
            CampaignStateName.INITIAL, CampaignStateName.REVIEW_READY);

    assertFalse(isValid);
  }

  @Test
  public void ifStateCanBeChanged_Invalid_State_And_Role_Has_No_Permission() {
    mockRequestAndContext();
    boolean isValid =
        campaignStateServiceImpl.ifStateCanBeChanged(
            CampaignStateName.INITIAL, CampaignStateName.ACTIVE);

    assertFalse(isValid);
  }

  @Test
  public void changeStateOrThrow_Should_Change_State() {

    Campaign campaign = CampaignUtils.TEST_CAMPAIGN.toBuilder().build();
    when(campaignStateRepository.findByName(CampaignStateName.REVIEW_READY))
        .thenReturn(Optional.of(CampaignUtils.TEST_REVIEW_READY_CAMPAIGN_STATE));

    campaignStateServiceImpl.changeStateOrThrow(campaign, CampaignStateName.REVIEW_READY);

    assertEquals(CampaignUtils.TEST_REVIEW_READY_CAMPAIGN_STATE, campaign.getCampaignState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void changeStateOrThrow_Should_Throw_When_Role_Has_No_Permission() {

    Campaign campaign = CampaignUtils.TEST_REVIEW_READY_CAMPAIGN.toBuilder().build();
    campaignStateServiceImpl.changeStateOrThrow(campaign, CampaignStateName.ACTIVE);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void changeStateOrThrow_Should_Throw_When_Invalid_State() {

    Campaign campaign = CampaignUtils.TEST_CAMPAIGN.toBuilder().build();
    campaignStateServiceImpl.changeStateOrThrow(campaign, CampaignStateName.ACTIVE);
  }
}
