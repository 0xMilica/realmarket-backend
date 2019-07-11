package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.FundraisingProposalRepository;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.util.AuditUtils;
import io.realmarket.propeler.util.AuthUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.util.AuditUtils.TEST_PENDING_REQUEST_STATE;
import static io.realmarket.propeler.util.FundraisingProposalUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FundraisingProposalServiceImpl.class)
public class FundraisingProposalServiceImplTest {

  @Mock private RequestStateService requestStateService;

  @Mock private FundraisingProposalRepository fundraisingProposalRepository;

  @Mock private EmailService emailService;

  @InjectMocks private FundraisingProposalServiceImpl fundraisingProposalServiceImpl;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContextAdmin();
  }

  @Test
  public void applyForFundraising_Should_Apply_For_Fundraising() {
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(TEST_PENDING_REQUEST_STATE);
    when(fundraisingProposalRepository.save(any(FundraisingProposal.class)))
        .thenReturn(TEST_PENDING_FUNDRAISING_PROPOSAL);

    FundraisingProposal actualFundraisingProposal =
        fundraisingProposalServiceImpl.applyForFundraising(TEST_FUNDRAISING_PROPOSAL_DTO);
    fundraisingProposalServiceImpl.applyForFundraising(TEST_FUNDRAISING_PROPOSAL_DTO);

    assertEquals(TEST_PENDING_REQUEST_STATE, actualFundraisingProposal.getRequestState());
  }

  @Test
  public void approveFundraisingProposal_Should_Approve() {
    when(fundraisingProposalRepository.getOne(TEST_FUNDRAISING_PROPOSAL_ID))
        .thenReturn(TEST_PENDING_FUNDRAISING_PROPOSAL.toBuilder().build());
    when(requestStateService.getRequestState(RequestStateName.APPROVED))
        .thenReturn(AuditUtils.TEST_APPROVED_REQUEST_STATE);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));
    when(fundraisingProposalRepository.save(any(FundraisingProposal.class)))
        .thenReturn(TEST_APPROVED_FUNDRAISING_PROPOSAL);

    fundraisingProposalServiceImpl.approveFundraisingProposal(TEST_FUNDRAISING_PROPOSAL_ID);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void approveFundraisingProposal_Should_Throw_If_Not_Admin() {
    AuthUtils.mockRequestAndContext();

    fundraisingProposalServiceImpl.approveFundraisingProposal(TEST_FUNDRAISING_PROPOSAL_ID);
  }

  @Test
  public void declineFundraisingProposal_Should_Decline() {
    when(fundraisingProposalRepository.getOne(TEST_FUNDRAISING_PROPOSAL_ID))
        .thenReturn(TEST_PENDING_FUNDRAISING_PROPOSAL.toBuilder().build());
    when(requestStateService.getRequestState(RequestStateName.DECLINED))
        .thenReturn(AuditUtils.TEST_DECLINED_REQUEST_STATE);
    when(fundraisingProposalRepository.save(any(FundraisingProposal.class)))
        .thenReturn(TEST_DECLINED_FUNDRAISING_PROPOSAL);

    fundraisingProposalServiceImpl.declineFundraisingProposal(
        TEST_FUNDRAISING_PROPOSAL_ID, TEST_AUDIT_DECLINE_DTO);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void declineFundraisingProposal_Should_Throw_If_Not_Admin() {
    AuthUtils.mockRequestAndContext();

    fundraisingProposalServiceImpl.declineFundraisingProposal(
        TEST_FUNDRAISING_PROPOSAL_ID, TEST_AUDIT_DECLINE_DTO);
  }
}
