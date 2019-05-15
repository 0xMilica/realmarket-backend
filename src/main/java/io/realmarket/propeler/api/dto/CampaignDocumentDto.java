package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.model.enums.ECampaignDocumentAccessLevel;
import io.realmarket.propeler.model.enums.ECampaignDocumentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "CampaignDocumentDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDocumentDto {

  @ApiModelProperty(value = "Campaign document's title")
  @NotBlank
  private String title;

  @ApiModelProperty(value = "Campaign document's access level")
  @NotNull
  private ECampaignDocumentAccessLevel accessLevel;

  @ApiModelProperty(value = "Campaign document's type")
  @NotNull
  private ECampaignDocumentType type;

  @ApiModelProperty(value = "Campaign document's URL")
  @NotBlank
  private String url;

  public CampaignDocumentDto(CampaignDocument campaignDocument) {
    this.title = campaignDocument.getTitle();
    this.accessLevel = campaignDocument.getAccessLevel();
    this.type = campaignDocument.getType();
    this.url = campaignDocument.getUrl();
  }

  public CampaignDocument buildCampaignDocument() {
    return CampaignDocument.builder()
        .title(this.title)
        .accessLevel(this.accessLevel)
        .type(this.type)
        .url(this.url)
        .build();
  }
}
