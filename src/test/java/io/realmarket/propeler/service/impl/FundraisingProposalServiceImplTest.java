package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.FundraisingProposalRepository;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.util.AuditUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.util.FundraisingProposalUtil.TEST_FUNDRAISING_PROPOSAL_DTO;
import static io.realmarket.propeler.util.FundraisingProposalUtil.TEST_PENDING_FUNDRAISING_PROPOSAL;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FundraisingProposalServiceImpl.class)
public class FundraisingProposalServiceImplTest {

  @Mock private RequestStateService requestStateService;

  @Mock private FundraisingProposalRepository fundraisingProposalRepository;

  @InjectMocks private FundraisingProposalServiceImpl applyForFundraisingImpl;

  @Test
  public void applyForFundraising_Should_Apply_For_Fundraising() {
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(AuditUtils.TEST_PENDING_REQUEST_STATE);
    when(fundraisingProposalRepository.save(any(FundraisingProposal.class)))
        .thenReturn(TEST_PENDING_FUNDRAISING_PROPOSAL);

    FundraisingProposal actualFundraisingProposal =
        applyForFundraisingImpl.applyForFundraising(TEST_FUNDRAISING_PROPOSAL_DTO);

    assertEquals(TEST_PENDING_FUNDRAISING_PROPOSAL, actualFundraisingProposal);
  }
}
