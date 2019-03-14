package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.FileService;
import io.realmarket.propeler.service.exception.StorageException;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

  @Value("${app.filename-length}")
  private int filenameLength;

  private final CloudObjectStorageService cloudObjectStorageService;

  @Autowired
  public FileServiceImpl(CloudObjectStorageService cloudObjectStorageService) {
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  public FileDto getFile(String fileName) {
    return cloudObjectStorageService.downloadFileDto(fileName);
  }

  public String uploadFile(MultipartFile file) {
    try {
      String generatedFileName =
          String.format(
              "%s.%s",
              RandomStringBuilder.generateBase32String(filenameLength),
              FilenameUtils.getExtension(file.getOriginalFilename()));

      cloudObjectStorageService.upload(
          generatedFileName, file.getInputStream(), Math.toIntExact(file.getSize()));

      return generatedFileName;
    } catch (IOException | ArithmeticException e) {
      log.error("Failed to store file {}", file.getOriginalFilename());
      throw new StorageException("Failed to store file", e);
    }
  }

  public void deleteFile(String fileName) {
    cloudObjectStorageService.delete(fileName);
  }
}