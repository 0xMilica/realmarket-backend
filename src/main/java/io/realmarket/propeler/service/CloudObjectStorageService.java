package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface CloudObjectStorageService {

  /**
   * @param fileName name of the file to be retrieved
   * @return Byte array of the retrieved file
   */
  byte[] download(String fileName);

  /**
   * @param fileName name of the file to be retrieved
   * @return true if file exists
   */
  boolean doesFileExist(String fileName);

  /**
   * @param fileName name of the file to be retrieved
   * @return Byte array of the retrieved file
   */
  FileDto downloadFileDto(String fileName);

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
   * @param oldName old name of file. Delete old name if it differs from current name.
   * @param name name of the file
   * @param file multipart file to upload
   */
  void uploadAndReplace(String oldName, String name, MultipartFile file);

  /**
   * Delete file with the provided name from the cloud object storage.
   *
   * @param fileName name of the file to be deleted
   */
  void delete(String fileName);
}
