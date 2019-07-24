package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Document;
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

  @ApiModelProperty(value = "Document's access level")
  private String accessLevel;

  @ApiModelProperty(value = "Document's url")
  private String url;

  @ApiModelProperty(value = "Document's upload date")
  private Instant uploadDate;

  public DocumentResponseDto(Document document) {
    this.id = document.getId();
    this.title = document.getTitle();
    this.type = document.getType().getName().name();
    this.accessLevel = document.getAccessLevel().getName().name();
    this.url = document.getUrl();
    this.uploadDate = document.getUploadDate();
  }
}
