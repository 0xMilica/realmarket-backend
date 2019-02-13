package io.realmarket.propeler.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface CloudObjectStorageService {

  /**
   * @param fileName name of the file to be retrieved
   * @return Byte array of the retrieved file
   */
  byte[] download(String fileName);

  /**
   * @param name name of the file
   * @param inputStream input stream of the file
   * @param fileSize size of the file
   */
  void upload(String name, InputStream inputStream, int fileSize);

  /**
   * @param name name of the file
   * @param file multipart file to upload
   */
  void upload(String name, MultipartFile file);

  /**
   * Delete file with the provided name from the cloud object storage.
   *
   * @param fileName name of the file to be deleted
   */
  void delete(String fileName);
}
