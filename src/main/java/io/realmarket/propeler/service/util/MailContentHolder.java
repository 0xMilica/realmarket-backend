package io.realmarket.propeler.service.util;

import io.realmarket.propeler.api.dto.enums.EEmailType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailContentHolder {
  private List<String> emails;
  @NotNull private EEmailType type;
  private Map<String, Object> content;
}
