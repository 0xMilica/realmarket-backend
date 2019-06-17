package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.InvestmentStateName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "investment_state",
    indexes = {@Index(columnList = "name", unique = true, name = "investment_state_uk_on_name")})
public class InvestmentState {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVESTMENT_STATE_SEQ")
  @SequenceGenerator(
      name = "INVESTMENT_STATE_SEQ",
      sequenceName = "INVESTMENT_STATE_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private InvestmentStateName name;
}
