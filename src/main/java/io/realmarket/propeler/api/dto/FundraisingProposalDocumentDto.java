package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.FundraisingProposalDocument;
import io.realmarket.propeler.model.enums.DocumentTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "FundraisingProposalDocumentDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundraisingProposalDocumentDto {

  @ApiModelProperty(value = "Fundraising proposal document's title")
  @NotBlank
  private String title;

  @ApiModelProperty(value = "Campaign document's type")
  @NotNull
  private DocumentTypeName type;

  @ApiModelProperty(value = "Campaign document's URL")
  @NotBlank
  private String url;

  public FundraisingProposalDocumentDto(FundraisingProposalDocument fundraisingProposalDocument) {
    this.title = fundraisingProposalDocument.getTitle();
    this.type = fundraisingProposalDocument.getType().getName();
    this.url = fundraisingProposalDocument.getUrl();
  }
}
