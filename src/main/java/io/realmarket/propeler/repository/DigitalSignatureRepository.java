package io.realmarket.propeler.repository;

import io.realmarket.propeler.api.dto.DigitalSignaturePublicDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.DigitalSignature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface DigitalSignatureRepository extends Repository<DigitalSignature, Long> {

  DigitalSignature save(DigitalSignature entity);

  @Query("SELECT ds FROM DigitalSignature ds WHERE ds.auth = :auth")
  Optional<DigitalSignature> getPrivateDigitalSignatureByAuth(@Param("auth") Auth auth);

  @Query(
      "SELECT NEW io.realmarket.propeler.api.dto.DigitalSignaturePublicDto(ds.id, ds.publicKey, ds.initialVector, ds.salt, ds.passLength, ds.auth.id) FROM DigitalSignature ds WHERE ds.auth = :auth")
  Optional<DigitalSignaturePublicDto> getPublicDigitalSignatureByAuth(@Param("auth") Auth auth);
}
