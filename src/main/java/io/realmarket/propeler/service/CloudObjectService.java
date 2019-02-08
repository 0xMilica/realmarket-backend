package io.realmarket.propeler.service;

import java.io.InputStream;

public interface CloudObjectService {

  /**
   * @param fileName name of the file to be retrieved
   * @return String Base64-encoded bytes of the retrieved file
   */
  byte[] download(String fileName);

  /**
   * @param name name of the file
   * @param inputStream input stream of the file
   * @param fileSize size of the file
   */
  void upload(String name, InputStream inputStream, int fileSize);

  /**
   * Delete file with the provided name from the cloud object storage.
   *
   * @param fileName name of the file to be deleted
   */
  void delete(String fileName);
}
