package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.EAuthorizationActionType;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "AuthorizedAction")
@Table(uniqueConstraints =  {@UniqueConstraint(columnNames = {"type", "authId"} , name = "authorized_action_uk_on_type_and_authId")})
@TypeDef(name = "eauthorizedactiontype", typeClass = PostgreSQLEnumType.class)
public class AuthorizedAction {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHORIZED_ACTION_SEQ")
  @SequenceGenerator(name = "AUTHORIZED_ACTION_SEQ", sequenceName = "AUTHORIZED_ACTION_SEQ", allocationSize = 1)
  private Long id;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "authorized_action_on_auth"))
  @ManyToOne
  private Auth auth;

  private String data;

  private Instant expiration;

  @Column(columnDefinition = "eauthorizedactiontype")
  @Type(type = "eauthorizedactiontype")
  @Enumerated(EnumType.STRING)
  private EAuthorizationActionType type;
}
