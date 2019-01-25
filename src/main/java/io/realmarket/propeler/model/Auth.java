package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Auth")
@Table(indexes = @Index(columnList = "username", unique = true))
public class Auth {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_SEQ")
  @SequenceGenerator(name = "AUTH_SEQ", sequenceName = "AUTH_SEQ", allocationSize = 1)
  private Long id;

  private String username;
  private String password;
  private String role;
  private Boolean active;
  private String registrationToken;
  private Date registrationTokenExpirationTime;
  private String resetToken;
  private Date resetTokenExpirationTime;
  private Boolean acceptedTermsOfUse;
  private String temporaryLoginToken;
  private Date temporaryLoginTokenExpirationTime;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "personId")
  private Person person;
}
