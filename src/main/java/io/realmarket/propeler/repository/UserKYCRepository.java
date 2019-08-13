package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserKYCRepository extends JpaRepository<UserKYC, Long> {

  Page<UserKYC> findAll(Pageable pageable);

  @Query(
      value =
          "select uk " +
              "from UserKYC uk left outer join Auth a on uk.user = a " +
              "where (:requestState is null or uk.requestState = :requestState) " +
              "and (:userRole is null or a.userRole = :userRole) " +
              "and (:isAssigned is null or ((:isAssigned = true and uk.auditor is not null)" +
              "or (:isAssigned = false and uk.auditor is null)))")
  Page<UserKYC> findAllByRequestStateAndByUserRoleAndByAssigned(
      Pageable pageable,
      @Param("requestState") RequestState requestState,
      @Param("userRole") UserRole userRole,
      @Param("isAssigned") Boolean isAssigned);
  Optional<UserKYC> findFirstByUserOrderByUploadDateDesc(Auth user);
}
