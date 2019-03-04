package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.repository.CompanyRepository;
import io.realmarket.propeler.service.impl.CompanyServiceImpl;
import io.realmarket.propeler.unit.util.CompanyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_USER_AUTH;
import static io.realmarket.propeler.unit.util.CompanyUtils.TEST_ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CompanyServiceImpl.class)
public class CompanyServiceImplTest {

  @Mock private CompanyRepository companyRepository;

  @InjectMocks private CompanyServiceImpl companyService;

  @Test
  public void Save_Should_CreateNewCompany() {
    Company companyMocked = CompanyUtils.getCompanyMocked();

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(TEST_USER_AUTH);
    SecurityContextHolder.setContext(securityContext);

    when(companyRepository.save(any(Company.class))).thenReturn(companyMocked);

    Company retVal = companyService.save(companyMocked);

    assertEquals(companyMocked, retVal);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void Save_Should_Throw_DataIntegrityViolationException() {
    Company companyMocked = CompanyUtils.getCompanyMocked();

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(TEST_USER_AUTH);
    SecurityContextHolder.setContext(securityContext);

    when(companyRepository.save(any(Company.class)))
        .thenThrow(DataIntegrityViolationException.class);

    companyService.save(companyMocked);
  }

  @Test
  public void FindByIdOrThrowException_Should_Return_CompanyDto() {
    Company companyMocked = CompanyUtils.getCompanyMocked();

    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.of(companyMocked));

    Company retVal = companyService.findByIdOrThrowException(TEST_ID);

    assertEquals(companyMocked, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByIdOrThrowException_Should_Throw_EntityNotFoundException() {
    when(companyRepository.findById(TEST_ID)).thenReturn(Optional.empty());

    companyService.findByIdOrThrowException(TEST_ID);
  }
}
