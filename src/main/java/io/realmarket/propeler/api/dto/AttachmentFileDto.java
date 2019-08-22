package io.realmarket.propeler.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentFileDto {

  private byte[] file;
  private String name;
  private String extension;
}
