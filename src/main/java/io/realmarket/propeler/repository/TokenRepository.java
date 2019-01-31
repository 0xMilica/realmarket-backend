package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByJwt(String jwt);
}
