package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  /**
   * Retrieve file with provided file name from the IBM cloud service.
   *
   * @param fileName name of the file to be retrieved
   * @return String Base64-encoded bytes of the retrieved file
   */
  FileDto getFile(String fileName);

  /**
   * Retrieve file with provided file name from the IBM cloud service. Check if file contains prefix
   * that marks it public
   *
   * @param fileName name of the file to be retrieved
   * @return Multipart file
   */
  byte[] getPublicFile(String fileName);

  /**
   * Stores file on the IBM cloud service and in the local storage.
   *
   * @param file file to be stored
   * @return name of the stored file
   */
  String uploadFile(MultipartFile file);

  /**
   * Deletes file with the provided file name from the cloud object storage.
   *
   * @param fileName name of the file to be retrieved
   */
  void deleteFile(String fileName);
}
