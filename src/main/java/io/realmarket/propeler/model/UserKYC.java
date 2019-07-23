package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "user_kyc")
public class UserKYC {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_KYC_SEQ")
  @SequenceGenerator(name = "USER_KYC_SEQ", sequenceName = "USER_KYC_SEQ", allocationSize = 1)
  private Long id;

  @JoinColumn(name = "auditorId", foreignKey = @ForeignKey(name = "user_kyc_fk_on_auditor"))
  @NotNull
  @ManyToOne
  private Auth auditor;

  @JoinColumn(name = "personId", foreignKey = @ForeignKey(name = "user_kyc_fk_on_person"))
  @NotNull
  @ManyToOne
  private Person person;

  @JoinColumn(
      name = "requestStateId",
      foreignKey = @ForeignKey(name = "user_kyc_fk_on_request_state"))
  @NotNull
  @ManyToOne
  private RequestState requestState;

  @Column(length = 10000)
  private String content;
}
