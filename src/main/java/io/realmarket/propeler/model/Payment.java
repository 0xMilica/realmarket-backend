package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Payment")
@Table(name = "payment")
@DiscriminatorColumn(name = "Entity_name")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_SEQ")
  @SequenceGenerator(name = "PAYMENT_SEQ", sequenceName = "PAYMENT_SEQ", allocationSize = 1)
  private Long id;

  @JoinColumn(name = "investmentId", foreignKey = @ForeignKey(name = "payment_fk_on_investment"))
  @ManyToOne
  private Investment investment;

  private BigDecimal amount;

  // TODO: Bind this field to currency entity later
  private String currency;

  @Builder.Default
  @Column(name = "creation_date")
  private Instant creationDate = Instant.now();

  @Column(name = "payment_date")
  private Instant paymentDate;

  public Payment(
      Investment investment,
      BigDecimal amount,
      String currency,
      Instant creationDate,
      Instant paymentDate) {
    this.investment = investment;
    this.amount = amount;
    this.currency = currency;
    this.creationDate = creationDate;
    this.paymentDate = paymentDate;
  }
}
