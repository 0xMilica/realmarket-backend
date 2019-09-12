package io.realmarket.propeler.service.util.email.message.custom.user;

import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.util.email.Parameters.*;

@Data
public class ResetPasswordMessage extends AbstractEmailMessage {

  private String frontendServiceUrlPath;

  public ResetPasswordMessage(
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
    String resetToken = (String) contentMap.get(RESET_TOKEN);
    if (resetToken == null) {
      throw new IllegalArgumentException(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }

    String resetPasswordLink =
        String.format("%s/auth/change-password?reset=%s", frontendServiceUrlPath, resetToken);

    Map<String, Object> data = getBasicEmailData();
    data.put(RESET_PASSWORD_LINK, resetPasswordLink);
    data.put(ILLUSTRATION, ILLUSTRATION);

    return data;
  }
}
