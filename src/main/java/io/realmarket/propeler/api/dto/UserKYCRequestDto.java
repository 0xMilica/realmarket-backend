package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(value = "UserKYCReqeustDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKYCRequestDto {

    List<String> documentsUrl;
    boolean politicallyExposed;
}
