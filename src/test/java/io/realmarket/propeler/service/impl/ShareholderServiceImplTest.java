package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.ShareholderRequestDto;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Shareholder;
import io.realmarket.propeler.repository.ShareholderRepository;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.PlatformSettingsService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CompanyUtils;
import io.realmarket.propeler.util.PlatformSettingsUtils;
import io.realmarket.propeler.util.ShareholderTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Optional;

import static io.realmarket.propeler.util.ShareholderTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ShareholderServiceImpl.class)
public class ShareholderServiceImplTest {

  @Mock private ShareholderRepository shareholderRepository;
  @Mock private CompanyService companyService;
  @Mock private PlatformSettingsService platformSettingsService;
  @Mock private ModelMapperBlankString modelMapperBlankString;

  @Mock private BlockchainMessageProducer blockchainMessageProducer;

  @InjectMocks private ShareholderServiceImpl shareholderService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void CreateShareholder_Should_CreateShareholder() throws Exception {
    Company company = CompanyUtils.getCompanyMocked();

    when(companyService.findMyCompany()).thenReturn(company);
    when(platformSettingsService.getPlatformCurrency())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_CURRENCY);

    shareholderService.createShareholder(createMockShareholderRequestDto());

    verify(shareholderRepository, times(1)).save(any());
  }

  @Test
  public void DeleteShareholder_Should_DeleteShareholder() {
    Shareholder shareholder = ShareholderTestUtils.createMockShareholder();
    Company company = CompanyUtils.getCompanyMocked();

    when(companyService.findMyCompany()).thenReturn(company);
    when(shareholderRepository.findById(shareholder.getId())).thenReturn(Optional.of(shareholder));

    shareholderService.deleteShareholder(shareholder.getId());

    verify(shareholderRepository, times(1)).delete(shareholder);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void DeleteShareholder_Should_ThrowException() {
    Shareholder shareholder = ShareholderTestUtils.createMockShareholder();
    Company company = CompanyUtils.getCompanyMocked();

    when(companyService.findMyCompany()).thenReturn(company);
    doThrow(ForbiddenOperationException.class).when(companyService).throwIfNotCompanyOwner();
    when(shareholderRepository.findById(shareholder.getId())).thenReturn(Optional.of(shareholder));

    shareholderService.deleteShareholder(shareholder.getId());
  }

  @Test
  public void PatchShareholder_Should_PatchChanges() {
    Shareholder shareholder = ShareholderTestUtils.createMockShareholder();
    ShareholderRequestDto shareholderRequestDto =
        ShareholderTestUtils.mockShareholderPatchLastName();
    Company company = CompanyUtils.getCompanyMocked();

    when(companyService.findMyCompany()).thenReturn(company);
    when(shareholderRepository.save(any(Shareholder.class))).thenReturn(shareholder);
    when(shareholderRepository.findById(shareholder.getId())).thenReturn(Optional.of(shareholder));
    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((Shareholder) args[1]).setName(shareholderRequestDto.getName());
              return null;
            })
        .when(modelMapperBlankString)
        .map(shareholderRequestDto, shareholder);

    shareholderService.patchShareholder(shareholder.getId(), shareholderRequestDto);

    assertEquals(TEST_SHAREHOLDER_NAME_2, shareholder.getName());
    verify(modelMapperBlankString, times(1)).map(shareholderRequestDto, shareholder);
    verify(shareholderRepository, times(1)).save(shareholder);
  }

  @Test
  public void GetShareholder_Should_ReturnListOfShareholders() {
    List<Shareholder> shareholdersList = mockShareholderList();
    Company company = CompanyUtils.getCompanyMocked();

    when(companyService.findMyCompany()).thenReturn(company);
    when(shareholderRepository.findAllByCompanyIdOrderByOrderNumberAsc(company.getId()))
        .thenReturn(shareholdersList);

    List<Shareholder> ret = shareholderService.getShareholders();
    assertEquals(shareholdersList, ret);
  }
}
