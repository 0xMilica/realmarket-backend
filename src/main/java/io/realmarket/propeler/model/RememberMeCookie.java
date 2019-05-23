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
@Entity
@Table(name = "remember_me_cookie")
public class RememberMeCookie {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMPORARY_TOKEN_SEQ")
  @SequenceGenerator(
      name = "TEMPORARY_TOKEN_SEQ",
      sequenceName = "TEMPORARY_TOKEN_SEQ",
      allocationSize = 1)
  private Long id;

  private String value;

  private Instant expirationTime;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "remember_me_cookie_fk_on_auth"))
  @ManyToOne
  private Auth auth;
}
