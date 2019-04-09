package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("File name.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilenameDto {
  String filename;
}
