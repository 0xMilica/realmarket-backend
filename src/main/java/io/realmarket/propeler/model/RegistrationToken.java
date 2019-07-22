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
@Entity(name = "RegistrationToken")
@Table(name = "registration_token")
public class RegistrationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGISTRATION_TOKEN_SEQ")
  @SequenceGenerator(
      name = "REGISTRATION_TOKEN_SEQ",
      sequenceName = "REGISTRATION_TOKEN_SEQ",
      allocationSize = 1)
  private Long id;

  private String value;

  private Instant expirationTime;

  @JoinColumn(
      name = "fundraisingProposalId",
      foreignKey = @ForeignKey(name = "registration_token_fk_on_fundraising_proposal"))
  @OneToOne
  private FundraisingProposal fundraisingProposal;
}
