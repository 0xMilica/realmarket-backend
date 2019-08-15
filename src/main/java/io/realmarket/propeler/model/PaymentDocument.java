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
@DiscriminatorValue("PaymentDocument")
@Entity(name = "PaymentDocument")
public class PaymentDocument extends Document {

  @JoinColumn(name = "paymentId", foreignKey = @ForeignKey(name = "document_fk_on_payment"))
  @ManyToOne
  private Payment payment;

  @Builder(builderMethodName = "paymentDocumentBuilder")
  public PaymentDocument(
      String title,
      DocumentAccessLevel accessLevel,
      DocumentType type,
      String url,
      Instant uploadDate,
      Payment payment) {
    super(title, accessLevel, type, url, uploadDate);
    this.payment = payment;
  }
}
