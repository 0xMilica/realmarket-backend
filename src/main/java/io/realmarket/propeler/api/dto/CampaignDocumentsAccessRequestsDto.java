package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(description = "CampaignDocumentsAccessRequestsDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDocumentsAccessRequestsDto {

  private CampaignResponseDto campaign;
  private List<CampaignDocumentsAccessRequestDto> requests;
}
