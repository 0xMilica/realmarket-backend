package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CompanyEditRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyEditRequestRepository extends JpaRepository<CompanyEditRequest, Long> {
}
