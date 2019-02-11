package io.realmarket.propeler.service;

import io.realmarket.propeler.service.util.MailContentHolder;

public interface EmailService {

  void sendMailToUser(MailContentHolder mailContentHolder);
}
