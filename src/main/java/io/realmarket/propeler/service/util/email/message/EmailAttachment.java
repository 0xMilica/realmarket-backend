package io.realmarket.propeler.service.util.email.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachment {
  private byte[] file;
  private String name;
  private String extension;
}
