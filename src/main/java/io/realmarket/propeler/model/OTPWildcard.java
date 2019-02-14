package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "OTPWildcard")
public class OTPWildcard {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OTP_WILDCARD")
  @SequenceGenerator(name = "OTP_WILDCARD", sequenceName = "OTP_WILDCARD", allocationSize = 1)
  private Long id;

  private String wildcard;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "otp_wildcard_on_auth"))
  @ManyToOne
  private Auth auth;

}
