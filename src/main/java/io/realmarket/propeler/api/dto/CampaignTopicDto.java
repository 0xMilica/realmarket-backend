package io.realmarket.propeler.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignTopicDto {

  @NotBlank(message = "Content must not be blank!")
  String content;
}
