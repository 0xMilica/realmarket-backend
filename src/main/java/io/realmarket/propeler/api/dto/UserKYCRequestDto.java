package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "UserKYCRequestDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKYCRequestDto {

  @Size(min = 1, message = "Please provide KYC documents")
  List<KYCDocumentDto> documents;

  @NotBlank(message = "Please provide user's politically status")
  boolean politicallyExposed;
}
