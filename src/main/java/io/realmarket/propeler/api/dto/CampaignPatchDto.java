package io.realmarket.propeler.api.dto;

import com.google.common.primitives.UnsignedInteger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(description = "Dto used to patch campaign data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampaignPatchDto {
  @ApiModelProperty(value = "Campaign funding goals")
  private Long fundingGoals;

  @ApiModelProperty(value = "Campaign time to raise funds")
  @Max(value = 90, message = "Please provide time to raise funds that is between 1 and 90 days")
  private UnsignedInteger timeToRaiseFunds;

  @ApiModelProperty(value = "Campaign min equity offered")
  @Digits(
      integer = 3,
      fraction = 2,
      message =
          "Min equity offered must be in range of 0.00 to 100.00 with max of 2 values behind decimal point")
  private BigDecimal minEquityOffered;

  @ApiModelProperty(value = "Campaign max equity offered")
  @Digits(
      integer = 3,
      fraction = 2,
      message =
          "Max equity offered must be in range of 0.00 to 100.00 with max of 2 values behind decimal point")
  private BigDecimal maxEquityOffered;

  @ApiModelProperty(value = "Campaign min equity offered")
  @NotNull
  private BigDecimal minInvestment;
}
