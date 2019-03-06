package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.time.Instant;

@ApiModel(value = "CampaignDocumentDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDocumentDto {

  @ApiModelProperty(value = "Campaign document's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

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

  @ApiModelProperty(value = "Campaign document's upload date")
  private Instant uploadDate;

  @ApiModelProperty(value = "Campaign document's campaign")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private CampaignDto campaign;

  public CampaignDocumentDto(CampaignDocument campaignDocument) {
    this.id = campaignDocument.getId();
    this.title = campaignDocument.getTitle();
    this.accessLevel = campaignDocument.getAccessLevel();
    this.type = campaignDocument.getType();
    this.url = campaignDocument.getUrl();
    this.uploadDate = campaignDocument.getUploadDate();
    this.campaign = new CampaignDto(campaignDocument.getCampaign());
  }

  public CampaignDocument buildCampaignDocument() {
    return CampaignDocument.builder()
        .title(this.title)
        .accessLevel(this.accessLevel)
        .type(this.type)
        .url(this.url)
        .uploadDate(this.uploadDate)
        .build();
  }
}
