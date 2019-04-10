package io.realmarket.propeler.service.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
  private FileUtils() {}

  public static String getExtensionOrThrowException(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is empty!");
    }

    String originalFileName = file.getOriginalFilename();
    String extension = FilenameUtils.getExtension(originalFileName);
    if (extension == null || extension.isEmpty()) {
      throw new IllegalArgumentException("File name not valid!.");
    }
    return extension;
  }
}
