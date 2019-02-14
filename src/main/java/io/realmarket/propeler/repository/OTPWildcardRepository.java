package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.OTPWildcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OTPWildcardRepository extends JpaRepository<OTPWildcard, Long> {

  void deleteAllByAuth(Auth auth);

  List<OTPWildcard> findAllByAuth(Auth auth);
}
