package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.repository.CompanyRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CompanyUtils;
import io.realmarket.propeler.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.util.CompanyUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CompanyServiceImpl.class)
@TestPropertySource(properties = {"cos.file_prefix.company_logo=company_logo/"})
public class CompanyServiceImplTest {

  @Mock private CompanyRepository companyRepository;

  @Mock private CloudObjectStorageService cloudObjectStorageService;

  @InjectMocks private CompanyServiceImpl companyService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void Save_Should_CreateNewCompany() {
    Company companyMocked = CompanyUtils.getCompanyMocked();

    when(companyRepository.save(any(Company.class))).thenReturn(companyMocked);

    Company retVal = companyService.save(companyMocked);

    assertEquals(companyMocked, retVal);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void Save_Should_Throw_DataIntegrityViolationException() {
    Company companyMocked = CompanyUtils.getCompanyMocked();

    when(companyRepository.save(any(Company.class)))
        .thenThrow(DataIntegrityViolationException.class);

    companyService.save(companyMocked);
  }

  @Test
  public void FindByIdOrThrowException_Should_Return_CompanyDto() {
    Company companyMocked = getCompanyMocked();

    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(companyMocked));

    Company retVal = companyService.findByIdOrThrowException(TEST_ID);

    assertEquals(companyMocked, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByIdOrThrowException_Should_Throw_EntityNotFoundException() {
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.empty());

    companyService.findByIdOrThrowException(TEST_ID);
  }

  @Test
  public void UploadLogo_Should_DeleteOldLogo_And_SaveToRepository() {

    Company company = getCompanyMocked();
    company.setLogoUrl(TEST_LOGO_URL);
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(company));

    companyService.uploadLogo(TEST_ID, FileUtils.MOCK_FILE_VALID);

    verify(cloudObjectStorageService, times(1))
        .uploadAndReplace(TEST_LOGO_URL, company.getLogoUrl(), FileUtils.MOCK_FILE_VALID);
    verify(companyRepository, times(1)).save(company);
  }

  @Test
  public void GetCompanyLogo_Should_ReturnLogo() {
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(getCompanyMocked()));
    when(cloudObjectStorageService.downloadFileDto(TEST_LOGO_URL))
        .thenReturn(FileUtils.TEST_FILE_DTO);

    FileDto returnFileDto = companyService.downloadLogo(TEST_ID);

    assertEquals(FileUtils.TEST_FILE_DTO, returnFileDto);
    verify(companyRepository, Mockito.times(1)).findById(TEST_ID);
    verify(cloudObjectStorageService, Mockito.times(1)).downloadFileDto(TEST_LOGO_URL);
  }

  @Test(expected = EntityNotFoundException.class)
  public void GetCompanyLogo_Should_Throw_EntityNotFoundException() {
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(getCompanyMocked()));
    doThrow(EntityNotFoundException.class).when(cloudObjectStorageService).downloadFileDto(any());

    companyService.downloadLogo(TEST_ID);
  }

  @Test
  public void DeleteCompanyLogo_Should_DeleteProfilePicture() {
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(getCompanyMocked()));
    doNothing().when(cloudObjectStorageService).delete(TEST_LOGO_URL);

    companyService.deleteLogo(TEST_ID);

    verify(companyRepository, Mockito.times(1)).findById(TEST_ID);
    verify(cloudObjectStorageService, Mockito.times(1)).delete(TEST_LOGO_URL);
  }

  @Test
  public void UploadFeaturedImage_Should_DeleteOldFeaturedImage_And_SaveToRepository() {
    Company company = getCompanyMocked();
    company.setFeaturedImageUrl(TEST_FEATURED_IMAGE_URL);
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(company));

    companyService.uploadFeaturedImage(TEST_ID, FileUtils.MOCK_FILE_VALID);

    verify(cloudObjectStorageService, times(1))
        .uploadAndReplace(
            TEST_FEATURED_IMAGE_URL, company.getFeaturedImageUrl(), FileUtils.MOCK_FILE_VALID);
    verify(companyRepository, times(1)).save(company);
  }

  @Test
  public void GetCompanyFeaturedImage_Should_ReturnFeaturedImage() {
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(getCompanyMocked()));
    when(cloudObjectStorageService.downloadFileDto(TEST_FEATURED_IMAGE_URL))
        .thenReturn(FileUtils.TEST_FILE_DTO);

    FileDto returnFileDto = companyService.downloadFeaturedImage(TEST_ID);

    assertEquals(FileUtils.TEST_FILE_DTO, returnFileDto);
    verify(companyRepository, Mockito.times(1)).findById(TEST_ID);
    verify(cloudObjectStorageService, Mockito.times(1)).downloadFileDto(TEST_FEATURED_IMAGE_URL);
  }

  @Test(expected = EntityNotFoundException.class)
  public void GetCompanyFeaturedImage_Should_Throw_EntityNotFoundException() {
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(getCompanyMocked()));
    doThrow(EntityNotFoundException.class).when(cloudObjectStorageService).downloadFileDto(any());

    companyService.downloadFeaturedImage(TEST_ID);
  }

  @Test
  public void DeleteCompanyFeaturedImage_Should_DeleteProfilePicture() {

    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(getCompanyMocked()));
    doNothing().when(cloudObjectStorageService).delete(TEST_FEATURED_IMAGE_URL);

    companyService.deleteFeaturedImage(TEST_ID);

    verify(companyRepository, Mockito.times(1)).findById(TEST_ID);
    verify(cloudObjectStorageService, Mockito.times(1)).delete(TEST_FEATURED_IMAGE_URL);
  }
}
