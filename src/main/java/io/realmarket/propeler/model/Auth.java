package io.realmarket.propeler.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "Auth")
@Table(indexes = @Index(columnList = "username", unique = true, name = "auth_uk_on_username"))
public class Auth {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_SEQ")
  @SequenceGenerator(name = "AUTH_SEQ", sequenceName = "AUTH_SEQ", allocationSize = 1)
  private Long id;

  private String username;
  private String password;

  @JoinColumn(name = "userRoleId", foreignKey = @ForeignKey(name = "auth_fk_on_user_role"))
  @ManyToOne
  private UserRole userRole;

  @JoinColumn(name = "stateId", foreignKey = @ForeignKey(name = "auth_fk_on_state"))
  @ManyToOne
  private AuthState state;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "personId", foreignKey = @ForeignKey(name = "auth_fk_on_person"))
  private Person person;

  private String totpSecret;

  private Boolean blocked;

  public Auth(Long id) {
    this.id = id;
  }
}
