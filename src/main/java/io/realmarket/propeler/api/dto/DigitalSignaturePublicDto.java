package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(
    value = "DigitalSignaturePublicDto",
    description = "Dto that contains public details of person's digital signature")
@Data
@AllArgsConstructor
@Builder
public class DigitalSignaturePublicDto {

  @ApiModelProperty(value = "Digital signature's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Public key")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @NotBlank
  private String publicKey;

  @ApiModelProperty(value = "Initial vector")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @NotBlank
  private String initialVector;

  @ApiModelProperty(value = "Salt")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @NotBlank
  private String salt;

  @ApiModelProperty(value = "Pass length")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @NotBlank
  private Integer passLength;

  @ApiModelProperty(value = "Digital signature's owner id")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long ownerId;
}
