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

  @ApiModelProperty(value = "Document's type")
  private String type;

  @ApiModelProperty(value = "Document's upload date")
  private Instant uploadDate;

  public DocumentResponseDto(CampaignDocument campaignDocument) {
    this.id = campaignDocument.getId();
    this.title = campaignDocument.getTitle();
    this.type = "campaign";
    this.uploadDate = campaignDocument.getUploadDate();
  }
}
