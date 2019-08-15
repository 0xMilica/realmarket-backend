package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Shareholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShareholderRepository extends JpaRepository<Shareholder, Long> {
  Optional<Shareholder> findById(Long id);

  List<Shareholder> findAllByCompanyIdOrderByOrderNumberAsc(Long companyId);

  Shareholder getByIdAndCompanyId(Long id, Long companyId);

  Integer countByCompanyId(Long companyId);

  @Query("Select MAX(s.orderNumber) FROM Shareholder s WHERE s.company = ?1")
  Integer getMaxOrder(Company company);
}
