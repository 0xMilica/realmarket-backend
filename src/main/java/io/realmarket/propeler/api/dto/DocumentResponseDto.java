package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.model.CompanyDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@ApiModel(description = "DocumentResponseDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentResponseDto {

  @ApiModelProperty(value = "Document's identifier")
  private Long id;

  @ApiModelProperty(value = "Document's title")
  private String title;

  @ApiModelProperty(value = "Kind of document")
  private String kind;

  @ApiModelProperty(value = "Document's access level")
  private String accessLevel;

  @ApiModelProperty(value = "Document's url")
  private String url;

  @ApiModelProperty(value = "Document's upload date")
  private Instant uploadDate;

  @ApiModelProperty(value = "Document's campaign name")
  private String campaignName;

  public DocumentResponseDto(CampaignDocument campaignDocument) {
    this.id = campaignDocument.getId();
    this.title = campaignDocument.getTitle();
    this.kind = campaignDocument.getType().getName().name();
    this.accessLevel = campaignDocument.getAccessLevel().getName().name();
    this.url = campaignDocument.getUrl();
    this.uploadDate = campaignDocument.getUploadDate();
    this.campaignName = campaignDocument.getCampaign().getName();
  }

  public DocumentResponseDto(CompanyDocument companyDocument) {
    this.id = companyDocument.getId();
    this.title = companyDocument.getTitle();
    this.kind = companyDocument.getType().getName().name();
    this.accessLevel = companyDocument.getAccessLevel().getName().name();
    this.url = companyDocument.getUrl();
    this.uploadDate = companyDocument.getUploadDate();
  }
}
