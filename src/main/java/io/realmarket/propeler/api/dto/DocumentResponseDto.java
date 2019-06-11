package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignDocument;
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

  @ApiModelProperty(value = "Type of document")
  private String type;

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
    this.type = "campaign";
    this.kind = campaignDocument.getType().getName().name();
    this.accessLevel = campaignDocument.getAccessLevel().getName().name();
    this.url = campaignDocument.getUrl();
    this.uploadDate = campaignDocument.getUploadDate();
    this.campaignName = campaignDocument.getCampaign().getName();
  }
}
