package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FileDto", description = "Model for retrieving files")
public class FileDto {
  @ApiModelProperty(value = "Extension of retrieved file")
  private String type;

  @ApiModelProperty(value = "Base64 encoded file")
  private String file;

  @Override
  public String toString() {
    return String.format("FileDto(type=%s, file=Base64EncodedFile)", type);
  }
}
