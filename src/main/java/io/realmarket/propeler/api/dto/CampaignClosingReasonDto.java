package io.realmarket.propeler.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignClosingReasonDto {

  @NotNull
  @Size(max = 250, message = "Closing reason cannot contain more than 250 characters")
  String closingReason;

  @NotNull boolean isSuccessful;
}
