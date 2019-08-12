package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.DigitalSignature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(
    value = "DigitalSignaturePrivateDto",
    description = "Dto that contains complete digital signature of the owner")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalSignaturePrivateDto {

  @ApiModelProperty(value = "Digital signature's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Encrypted private key")
  @NotBlank
  private String encryptedPrivateKey;

  @ApiModelProperty(value = "Public key")
  @NotBlank
  private String publicKey;

  @ApiModelProperty(value = "Initial vector")
  @NotBlank
  private String initialVector;

  @ApiModelProperty(value = "Salt")
  @NotBlank
  private String salt;

  @ApiModelProperty(value = "Pass length")
  @NotBlank
  private Integer passLength;

  @ApiModelProperty(value = "Digital signature's owner identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long ownerId;

  public DigitalSignaturePrivateDto(DigitalSignature digitalSignature) {
    this.id = digitalSignature.getId();
    this.encryptedPrivateKey = digitalSignature.getEncryptedPrivateKey();
    this.publicKey = digitalSignature.getPublicKey();
    this.initialVector = digitalSignature.getInitialVector();
    this.salt = digitalSignature.getSalt();
    this.passLength = digitalSignature.getPassLength();
    this.ownerId = digitalSignature.getAuth().getId();
  }
}
