package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "OTPWildcard")
@Table(name = "otp_wildcard")
public class OTPWildcard {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OTP_WILDCARD_SEQ")
  @SequenceGenerator(
      name = "OTP_WILDCARD_SEQ",
      sequenceName = "OTP_WILDCARD_SEQ",
      allocationSize = 1)
  private Long id;

  private String wildcard;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "otp_wildcard_on_auth"))
  @ManyToOne
  private Auth auth;
}
