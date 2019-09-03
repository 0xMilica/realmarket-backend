package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.service.util.email.message.EmailAttachment;

import java.util.List;
import java.util.Map;

public interface EmailService {

  void sendEmailToUser(
      EmailType emailType, List<String> addressList, Map<String, Object> contentMap);

  void sendEmailToUser(
      EmailType emailType,
      List<String> addressList,
      Map<String, Object> contentMap,
      EmailAttachment emailAttachment);
}
