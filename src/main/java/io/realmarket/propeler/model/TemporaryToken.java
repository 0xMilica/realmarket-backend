package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.model.enums.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

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
          columnNames = {"temporaryTokenType", "authId"},
          name = "token_uk_on_temporaryTokenType_and_authId")
    })
@TypeDef(name = "etemporarytokentype", typeClass = PostgreSQLEnumType.class)
public class TemporaryToken {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMPORARY_TOKEN_SEQ")
  @SequenceGenerator(
      name = "TEMPORARY_TOKEN_SEQ",
      sequenceName = "TEMPORARY_TOKEN_SEQ",
      allocationSize = 1)
  private Long id;

  private String value;

  @Column(columnDefinition = "etemporarytokentype")
  @Type(type = "etemporarytokentype")
  @Enumerated(EnumType.STRING)
  private ETemporaryTokenType temporaryTokenType;

  private Instant expirationTime;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "token_fk1_on_auth"))
  @ManyToOne
  private Auth auth;
}
