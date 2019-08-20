package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "Investment")
public class Investment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVESTMENT_SEQ")
  @SequenceGenerator(name = "INVESTMENT_SEQ", sequenceName = "INVESTMENT_SEQ", allocationSize = 1)
  private Long id;

  private BigDecimal investedAmount;
  private String currency;

  @JoinColumn(name = "personId", foreignKey = @ForeignKey(name = "investment_fk_on_person"))
  @ManyToOne
  private Person person;

  @JoinColumn(name = "campaignId", foreignKey = @ForeignKey(name = "investment_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;

  @JoinColumn(
      name = "investmentStateId",
      foreignKey = @ForeignKey(name = "investment_fk_on_investment_state"))
  @ManyToOne
  private InvestmentState investmentState;

  @Builder.Default
  @Column(name = "creation_date")
  private Instant creationDate = Instant.now();

  @Column(name = "payment_date")
  private Instant paymentDate;

  private String invoiceUrl;
}
