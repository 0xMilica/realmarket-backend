package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.CampaignApplication;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.CampaignApplicationRepository;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.util.AuditUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.util.CampaignApplicationUtil.TEST_CAMPAIGN_APPLICATION_DTO;
import static io.realmarket.propeler.util.CampaignApplicationUtil.TEST_PENDING_CAMPAIGN_APPLICATION;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CampaignApplicationServiceImpl.class)
public class CampaignApplicationServiceImplTest {

  @Mock private RequestStateService requestStateService;

  @Mock private CampaignApplicationRepository campaignApplicationRepository;

  @InjectMocks private CampaignApplicationServiceImpl campaignApplicationServiceImpl;

  @Test
  public void applyForCampaign_Should_Apply_For_Campaign() {
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(AuditUtils.TEST_PENDING_REQUEST_STATE);
    when(campaignApplicationRepository.save(any(CampaignApplication.class)))
        .thenReturn(TEST_PENDING_CAMPAIGN_APPLICATION);

    CampaignApplication actualCampaignApplication =
        campaignApplicationServiceImpl.applyForCampaign(TEST_CAMPAIGN_APPLICATION_DTO);

    assertEquals(TEST_PENDING_CAMPAIGN_APPLICATION, actualCampaignApplication);
  }
}
