package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "KYCDocumentDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KYCDocumentDto {

  String name;
  String url;
  String type;
}
