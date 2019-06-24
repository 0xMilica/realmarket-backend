package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value = "Dto used for campaign update")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignUpdateDto {

  @ApiModelProperty(value = "Campaign update title")
  @Nullable
  private String title;

  @ApiModelProperty(value = "Campaign update content")
  @NotBlank(message = "Content must not be blank!")
  @Size(max = 10000, message = "Campaign update content cannot be longer than 10000 characters.")
  private String content;

  public CampaignUpdateDto(CampaignUpdate campaignUpdate) {
    this.title = campaignUpdate.getTitle();
    this.content = campaignUpdate.getContent();
  }
}
