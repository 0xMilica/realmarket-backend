package io.realmarket.propeler.e2e.util;

import io.realmarket.propeler.e2e.FileTest;
import org.springframework.http.MediaType;

import java.io.File;

import static io.restassured.RestAssured.given;

public class FileUtils {
  private FileUtils() {}

  public static final String TEST_FILE_TYPE = "PDF";

  public static final String TEST_FILE_NAME = "TEST_FILE_NAME." + TEST_FILE_TYPE;
  public static final byte[] TEST_FILE_BYTES = "TEST_FILE".getBytes();

  public static String uploadFile(File file) {
    return given()
        .accept(MediaType.TEXT_PLAIN_VALUE)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .when()
        .multiPart(file)
        .post(FileTest.ENDPOINT)
        .getBody()
        .print();
  }
}
