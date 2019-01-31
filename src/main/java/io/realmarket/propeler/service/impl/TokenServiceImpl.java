package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Token;
import io.realmarket.propeler.repository.TokenRepository;
import io.realmarket.propeler.service.JWTService;
import io.realmarket.propeler.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

  @Value("${access-token.expiration-time}")
  private long tokenExpirationDate;

  private final TokenRepository tokenRepository;
  private final JWTService jwtService;

  @Autowired
  public TokenServiceImpl(TokenRepository tokenRepository, JWTService jwtService) {
    this.tokenRepository = tokenRepository;
    this.jwtService = jwtService;
  }

  public Token findByJwtOrThrowException(String jwt) {
    return tokenRepository
        .findByJwt(jwt)
        .orElseThrow(() -> new EntityNotFoundException("Auth with provided JWT does not exist."));
  }

  public Token createToken(Auth auth) {
    return tokenRepository.save(
        Token.builder()
            .auth(auth)
            .jwt(jwtService.generateJWT(auth))
            .expirationTime(new Date(System.currentTimeMillis() + tokenExpirationDate))
            .build());
  }
}
