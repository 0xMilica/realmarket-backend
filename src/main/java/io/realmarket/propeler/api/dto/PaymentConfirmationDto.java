package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "PaymentConfirmationDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentConfirmationDto {

  String documentUrl;
  String documentTitle;
  Date paymentDate;
}
