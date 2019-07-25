package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.CampaignDocumentsAccessRequestRepository;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignDocumentUtils;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class CampaignDocumentsAccessRequestServiceImplTest {

  @Mock private CampaignDocumentsAccessRequestRepository campaignDocumentsAccessRequestRepository;
  @Mock private RequestStateService requestStateService;
  @Mock private CampaignService campaignService;
  @Mock private AuthService authService;

  @InjectMocks
  private CampaignDocumentsAccessRequestServiceImpl campaignDocumentsAccessRequestService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void sendDocumentsAccessRequest_Should_CreateDocumentsAccessRequest() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_ACTIVE_CAMPAIGN);

    when(authService.findById(AuthUtils.TEST_AUTH_ID)).thenReturn(Optional.of(AuthUtils.TEST_AUTH));
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(CampaignDocumentUtils.TEST_PENDING_REQUEST_STATE);
    when(campaignDocumentsAccessRequestRepository.save(any()))
        .thenReturn(CampaignDocumentUtils.TEST_PENDING_CAMPAIGN_DOCUMENTS_ACCESS_REQUEST);

    campaignDocumentsAccessRequestService.sendCampaignDocumentsAccessRequest(
        CampaignUtils.TEST_URL_FRIENDLY_NAME);

    verify(campaignDocumentsAccessRequestRepository, times(1)).save(any());
  }

  @Test
  public void acceptDocumentsAccessRequest_Should_ApproveDocumentsAccessRequest() {
    when(campaignDocumentsAccessRequestRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenReturn(
            Optional.of(CampaignDocumentUtils.TEST_PENDING_CAMPAIGN_DOCUMENTS_ACCESS_REQUEST));
    when(requestStateService.getRequestState(RequestStateName.APPROVED))
        .thenReturn(CampaignDocumentUtils.TEST_APPROVED_REQUEST_STATE);
    when(campaignDocumentsAccessRequestRepository.save(any()))
        .thenReturn(CampaignDocumentUtils.TEST_ACCEPTED_CAMPAIGN_DOCUMENTS_ACCESS_REQUEST);

    campaignDocumentsAccessRequestService.acceptCampaignDocumentsAccessRequest(
        CampaignDocumentUtils.TEST_ID);

    verify(campaignDocumentsAccessRequestRepository, times(1)).save(any());
  }
}
