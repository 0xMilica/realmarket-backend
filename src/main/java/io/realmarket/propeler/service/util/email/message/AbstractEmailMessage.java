package io.realmarket.propeler.service.util.email.message;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.util.email.Parameters.*;

@Data
public abstract class AbstractEmailMessage {
  protected List<String> addressList;
  protected Map<String, Object> contentMap;
  protected String subject;
  protected EmailAttachment attachment;
  protected String templateName;

  public AbstractEmailMessage(
      List<String> addressList,
      Map<String, Object> contentMap,
      String subject,
      String templateName) {
    this.addressList = addressList;
    this.contentMap = contentMap;
    this.subject = subject;
    this.templateName = templateName;
  }

  public AbstractEmailMessage(
      List<String> addressList,
      Map<String, Object> contentMap,
      String subject,
      EmailAttachment attachment,
      String templateName) {
    this.addressList = addressList;
    this.contentMap = contentMap;
    this.subject = subject;
    this.templateName = templateName;
    this.attachment = attachment;
  }

  public abstract Map<String, Object> getData();

  protected Map<String, Object> getBasicEmailData() {
    Map<String, Object> data = new HashMap<>();
    data.put(LOGO, LOGO);
    data.put(TWITTER, TWITTER);
    data.put(FACEBOOK, FACEBOOK);
    data.put(YOUTUBE, YOUTUBE);
    data.put(LINKEDIN, LINKEDIN);
    return data;
  }
}
