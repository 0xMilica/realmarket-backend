package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.DocumentTypeName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(description = "DocumentEntityType")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentMetadata {

  private DocumentTypeName type;
  private boolean mandatory;
  private List<DocumentAccessLevelName> accessLevels;
  private Integer maxUploads;
}
