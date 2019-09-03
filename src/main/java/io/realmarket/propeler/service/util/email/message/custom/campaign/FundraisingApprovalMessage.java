package io.realmarket.propeler.service.util.email.message.custom.campaign;

import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.util.email.Parameters.*;

@Data
public class FundraisingApprovalMessage extends AbstractEmailMessage {

  private String frontendServiceUrlPath;

  public FundraisingApprovalMessage(
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
    String registrationToken = (String) contentMap.get(REGISTRATION_TOKEN);
    if (registrationToken == null) {
      throw new IllegalArgumentException(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }

    String registrationLink =
        String.format("%s/auth/register/%s", frontendServiceUrlPath, registrationToken);
    Map<String, Object> data = getBasicEmailData();
    data.putAll(contentMap);
    data.put(CHECK_CIRCLE, CHECK_CIRCLE);
    data.put(REGISTRATION_LINK, registrationLink);

    return data;
  }
}
