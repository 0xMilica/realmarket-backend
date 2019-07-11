package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.FundraisingProposalDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@ApiModel(value = "FundraisingProposalDocumentResponseDto")
@NoArgsConstructor
@AllArgsConstructor
public class FundraisingProposalDocumentResponseDto extends FundraisingProposalDocumentDto {

  @ApiModelProperty(value = "Fundraising proposal document's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Fundraising proposal document's upload date")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant uploadDate;

  public FundraisingProposalDocumentResponseDto(
      FundraisingProposalDocument fundraisingProposalDocument) {
    super(fundraisingProposalDocument);
    this.uploadDate = fundraisingProposalDocument.getUploadDate();
    this.id = fundraisingProposalDocument.getId();
  }
}
