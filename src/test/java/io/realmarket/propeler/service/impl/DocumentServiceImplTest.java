package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.service.exception.BadRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class DocumentServiceImplTest {

  @InjectMocks private DocumentServiceImpl documentService;

  @Test
  public void getTypes_Should_ReturnCampaignDocumentTypes() {
    documentService.getTypes("campaigns");
  }

  @Test(expected = BadRequestException.class)
  public void getTypes_Should_Throw_BadRequestException() {
    documentService.getTypes("abc");
  }
}
