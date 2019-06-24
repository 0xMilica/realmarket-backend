package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.Shareholder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel(description = "Dto used for public shareholder data display")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareholderPublicResponseDto {
  @ApiModelProperty(value = "Name of shareholder")
  private String name;

  @ApiModelProperty(value = "Location of shareholder")
  private String location;

  @ApiModelProperty(value = "Invested amount")
  private BigDecimal investedAmount;

  @ApiModelProperty(value = "Short description of shareholder")
  private String description;

  @ApiModelProperty(value = "Photo url")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String photoUrl;

  @ApiModelProperty(value = "Linkedin url")
  private String linkedinUrl;

  @ApiModelProperty(value = "Twitter url")
  private String twitterUrl;

  @ApiModelProperty(value = "Facebook url")
  private String facebookUrl;

  @ApiModelProperty(value = "Custom url")
  private String customProfileUrl;

  @ApiModelProperty(value = "Company where shareholder belongs to")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long companyId;

  public ShareholderPublicResponseDto(Shareholder shareholder) {
    if (shareholder.getIsAnonymous()) {
      this.investedAmount = shareholder.getInvestedAmount();
      return;
    }
    this.name = shareholder.getName();
    this.location = shareholder.getLocation();
    this.investedAmount = shareholder.getInvestedAmount();
    this.description = shareholder.getDescription();
    this.photoUrl = shareholder.getPhotoUrl();
    this.linkedinUrl = shareholder.getLinkedinUrl();
    this.twitterUrl = shareholder.getTwitterUrl();
    this.facebookUrl = shareholder.getFacebookUrl();
    this.customProfileUrl = shareholder.getCustomProfileUrl();
    this.companyId = shareholder.getCompany().getId();
  }
}
