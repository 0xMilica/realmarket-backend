package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKYCRepository extends JpaRepository<UserKYC, Long> {

  Page<UserKYC> findAll(Pageable pageable);

  @Query(
      value =
          "select uk from UserKYC uk where uk.requestState = :requestState and uk.auditor <> null")
  Page<UserKYC> findAllByRequestStateAssigned(
      Pageable pageable, @Param("requestState") RequestState requestState);

  @Query(
      value =
          "select uk from UserKYC uk where uk.requestState = :requestState and uk.auditor = null")
  Page<UserKYC> findAllByRequestStateNotAssigned(
      Pageable pageable, @Param("requestState") RequestState requestState);

  @Query(
      value =
          "select uk from UserKYC uk left join Auth a on uk.user = a where a.userRole = :userRole")
  Page<UserKYC> findAllByUserRole(Pageable pageable, @Param("userRole") UserRole userRole);

  @Query(
      value =
          "select uk from UserKYC uk left outer join Auth a on uk.user = a where uk.requestState = :requestState and a.userRole = :userRole and uk.requestState = :requestState and uk.auditor <> null")
  Page<UserKYC> findAllByRequestStateAndByUserRoleAssigned(
      Pageable pageable,
      @Param("requestState") RequestState requestState,
      @Param("userRole") UserRole userRole);

  @Query(
      value =
          "select uk from UserKYC uk left join Auth a  on uk.user = a where uk.requestState = :requestState and a.userRole = :userRole and uk.requestState = :requestState and uk.auditor = null")
  Page<UserKYC> findAllByRequestStateAndByUserRoleNotAssigned(
      Pageable pageable,
      @Param("requestState") RequestState requestState,
      @Param("userRole") UserRole userRole);
}
