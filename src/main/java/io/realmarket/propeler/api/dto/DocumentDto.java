package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Document;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.DocumentTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "DocumentDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {

  @ApiModelProperty(value = "Document's title")
  @NotBlank
  private String title;

  @ApiModelProperty(value = "Document's access level")
  @NotNull
  private DocumentAccessLevelName accessLevel;

  @ApiModelProperty(value = "Document's type")
  @NotNull
  private DocumentTypeName type;

  @ApiModelProperty(value = "Document's URL")
  @NotBlank
  private String url;

  public DocumentDto(Document document) {
    this.title = document.getTitle();
    this.accessLevel = document.getAccessLevel().getName();
    this.type = document.getType().getName();
    this.url = document.getUrl();
  }
}
