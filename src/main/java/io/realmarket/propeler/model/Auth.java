package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.EAuthState;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.model.enums.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "Auth")
@Table(indexes = @Index(columnList = "username", unique = true, name = "auth_uk_on_username"))
@TypeDef(name = "euserrole", typeClass = PostgreSQLEnumType.class)
@TypeDef(name = "eauthstate", typeClass = PostgreSQLEnumType.class)
public class Auth {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_SEQ")
  @SequenceGenerator(name = "AUTH_SEQ", sequenceName = "AUTH_SEQ", allocationSize = 1)
  private Long id;

  private String username;
  private String password;

  @Column(columnDefinition = "euserrole")
  @Type(type = "euserrole")
  @Enumerated(EnumType.STRING)
  private EUserRole userRole;

  @Column(columnDefinition = "eauthstate")
  @Type(type = "eauthstate")
  @Enumerated(EnumType.STRING)
  private EAuthState state;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "personId", foreignKey = @ForeignKey(name = "auth_fk1_on_person"))
  private Person person;

  private String totpSecret;

  public Auth(Long id) {
    this.id = id;
  }
}
