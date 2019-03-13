package io.realmarket.propeler.service.util.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaResponseDto {

  private Boolean success;
  private Instant timestamp;
  private String hostname;

  @JsonProperty("error-codes")
  private List<String> errorCodes;
}
