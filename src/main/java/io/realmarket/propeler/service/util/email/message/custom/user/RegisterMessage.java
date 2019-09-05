package io.realmarket.propeler.service.util.email.message.custom.user;

import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.util.email.Parameters.*;

@Data
public class RegisterMessage extends AbstractEmailMessage {

  private String frontendServiceUrlPath;

  public RegisterMessage(
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
    String activationToken = (String) contentMap.get(ACTIVATION_TOKEN);
    if (activationToken == null) {
      throw new IllegalArgumentException(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }

    String activationLink =
        String.format(
            "%s/auth/confirm-registration?registrationToken=%s",
            frontendServiceUrlPath, activationToken);

    Map<String, Object> data = getBasicEmailData();
    data.put(USERNAME, contentMap.get(USERNAME));
    data.put(ACTIVATION_LINK, activationLink);

    return data;
  }
}
