package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.service.CampaignDocumentService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignDocumentUtils;
import io.realmarket.propeler.util.CampaignUtils;
import io.realmarket.propeler.util.CompanyUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class DocumentServiceImplTest {

  @Mock private CompanyService companyService;
  @Mock private CampaignService campaignService;
  @Mock private CampaignDocumentService campaignDocumentService;

  @InjectMocks private DocumentServiceImpl documentServiceImpl;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void getDocuments_Should_ReturnListOfDocuments() {
    Company companyMocked = CompanyUtils.getCompanyMocked();
    Campaign campaignMocked = CampaignUtils.getCampaignMocked();
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked();

    when(companyService.findByAuthIdOrThrowException(AuthUtils.TEST_AUTH_ID))
        .thenReturn(companyMocked);
    when(campaignService.findAllByCompany(companyMocked)).thenReturn(Arrays.asList(campaignMocked));
    when(campaignDocumentService.findAllByCampaign(campaignMocked))
        .thenReturn(Arrays.asList(campaignDocumentMocked));

    assertNotNull(documentServiceImpl.getDocuments(AuthUtils.TEST_AUTH_ID));
  }
}
