package io.realmarket.propeler.service.util.email.message.general;

import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import io.realmarket.propeler.service.util.email.message.EmailAttachment;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.util.email.Parameters.CHECK_CIRCLE;
import static io.realmarket.propeler.service.util.email.Parameters.DASHBOARD_LINK;

@Data
public class ApprovalMessage extends AbstractEmailMessage {

  private String frontendServiceUrlPath;

  public ApprovalMessage(
      List<String> addressList,
      Map<String, Object> contentMap,
      String subject,
      String templateName,
      String frontendServiceUrlPath) {
    super(addressList, contentMap, subject, templateName);
    this.frontendServiceUrlPath = frontendServiceUrlPath;
  }

  public ApprovalMessage(
      List<String> addressList,
      Map<String, Object> contentMap,
      String subject,
      EmailAttachment emailAttachment,
      String templateName,
      String frontendServiceUrlPath) {
    super(addressList, contentMap, subject, emailAttachment, templateName);
    this.frontendServiceUrlPath = frontendServiceUrlPath;
  }

  @Override
  public Map<String, Object> getData() {
    Map<String, Object> data = getBasicEmailData();
    data.putAll(contentMap);
    data.put(CHECK_CIRCLE, CHECK_CIRCLE);
    data.put(DASHBOARD_LINK, frontendServiceUrlPath);

    return data;
  }
}
