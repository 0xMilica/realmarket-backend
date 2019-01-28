package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.EmailDto;

public interface EmailService {

  void sendMailToUser(EmailDto emailDto);
}
