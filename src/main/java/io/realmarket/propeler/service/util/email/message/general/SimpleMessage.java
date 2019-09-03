package io.realmarket.propeler.service.util.email.message.general;

import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import io.realmarket.propeler.service.util.email.message.EmailAttachment;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SimpleMessage extends AbstractEmailMessage {

  public SimpleMessage(
      List<String> addressList,
      Map<String, Object> contentMap,
      String subject,
      String templateName) {
    super(addressList, contentMap, subject, templateName);
  }

  public SimpleMessage(
      List<String> addressList,
      Map<String, Object> contentMap,
      String subject,
      EmailAttachment emailAttachment,
      String templateName) {
    super(addressList, contentMap, subject, emailAttachment, templateName);
  }

  @Override
  public Map<String, Object> getData() {
    Map<String, Object> data = getBasicEmailData();
    data.putAll(contentMap);
    return data;
  }
}
