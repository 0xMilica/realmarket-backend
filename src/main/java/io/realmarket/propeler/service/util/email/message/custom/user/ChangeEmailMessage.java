package io.realmarket.propeler.service.util.email.message.custom.user;

import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.util.email.Parameters.EMAIL_CHANGE_TOKEN;
import static io.realmarket.propeler.service.util.email.Parameters.ILLUSTRATION;

@Data
public class ChangeEmailMessage extends AbstractEmailMessage {

  private String frontendServiceUrlPath;

  public ChangeEmailMessage(
      List<String> addressList,
      Map<String, Object> contentMap,
      String subject,
      String templateName,
      String frontendServiceUrlPath) {
    super(addressList, contentMap, subject, templateName);
    this.frontendServiceUrlPath = frontendServiceUrlPath;
  }

  @Override
  public Map<String, Object> getData() {
    String token = (String) contentMap.get(EMAIL_CHANGE_TOKEN);
    if (token == null) {
      throw new IllegalArgumentException(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }

    String changeEmailLink =
        String.format("%s/auth/change-email?emailChangeToken=%s", frontendServiceUrlPath, token);

    Map<String, Object> data = getBasicEmailData();
    data.put(EMAIL_CHANGE_TOKEN, changeEmailLink);
    data.put(ILLUSTRATION, ILLUSTRATION);

    return data;
  }
}
