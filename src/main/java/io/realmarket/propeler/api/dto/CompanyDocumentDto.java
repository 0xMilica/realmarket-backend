package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CompanyDocument;
import io.realmarket.propeler.model.enums.ECompanyDocumentType;
import io.realmarket.propeler.model.enums.EDocumentAccessLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "CompanyDocumentDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDocumentDto {

  @ApiModelProperty(value = "Company document's title")
  @NotBlank
  private String title;

  @ApiModelProperty(value = "Campaign document's access level")
  @NotNull
  private EDocumentAccessLevel accessLevel;

  @ApiModelProperty(value = "Campaign document's type")
  @NotNull
  private ECompanyDocumentType type;

  @ApiModelProperty(value = "Campaign document's URL")
  @NotBlank
  private String url;

  public CompanyDocumentDto(CompanyDocument companyDocument) {
    this.title = companyDocument.getTitle();
    this.accessLevel = companyDocument.getAccessLevel().getName();
    this.type = companyDocument.getType().getName();
    this.url = companyDocument.getUrl();
  }
}
