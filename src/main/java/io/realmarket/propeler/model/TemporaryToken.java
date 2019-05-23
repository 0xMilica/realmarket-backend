package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "TemporaryToken")
@Table(
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"temporaryTokenTypeId", "authId"},
          name = "temporary_token_uk_on_temporary_token_type_and_auth")
    })
public class TemporaryToken {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMPORARY_TOKEN_SEQ")
  @SequenceGenerator(
      name = "TEMPORARY_TOKEN_SEQ",
      sequenceName = "TEMPORARY_TOKEN_SEQ",
      allocationSize = 1)
  private Long id;

  private String value;

  @JoinColumn(
      name = "temporaryTokenTypeId",
      foreignKey = @ForeignKey(name = "temporary_token_fk_on_temporary_token_type"))
  @ManyToOne
  private TemporaryTokenType temporaryTokenType;

  private Instant expirationTime;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "temporary_token_fk_on_auth"))
  @ManyToOne
  private Auth auth;
}
