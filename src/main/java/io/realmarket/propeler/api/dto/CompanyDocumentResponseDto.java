package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.CompanyDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@ApiModel(value = "CompanyDocumentResponseDto")
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDocumentResponseDto extends CompanyDocumentDto {

  @ApiModelProperty(value = "Company document's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Company document's upload date")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant uploadDate;

  public CompanyDocumentResponseDto(CompanyDocument companyDocument) {
    super(companyDocument);
    this.uploadDate = companyDocument.getUploadDate();
    this.id = companyDocument.getId();
  }
}
