package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CardPayment;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@ApiModel(value = "CardPaymentResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardPaymentResponseDto {

  Long cardPaymentId;

  Long investmentId;

  BigDecimal amount;

  // TODO: Bind this field to currency entity later
  String currency;

  Instant creationDate;

  Instant paymentDate;

  String sessionToken;

  public CardPaymentResponseDto(CardPayment cardPayment) {
    this.cardPaymentId = cardPayment.getId();
    this.investmentId = cardPayment.getInvestment().getId();
    this.amount = cardPayment.getAmount();
    this.currency = cardPayment.getCurrency();
    this.creationDate = cardPayment.getCreationDate();
    this.paymentDate = cardPayment.getPaymentDate();
    this.sessionToken = cardPayment.getSessionToken();
  }
}
