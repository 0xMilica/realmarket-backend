package io.realmarket.propeler.e2e;

import io.realmarket.propeler.e2e.util.IntegrationTestEnvironmentPreparator;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static io.realmarket.propeler.e2e.util.FileUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class FileTest extends IntegrationTestEnvironmentPreparator {

  public static final String ENDPOINT = "/api/files";
  private static final String MEDIA_TYPE =
      MediaType.toString(Arrays.asList(MediaType.ALL, MediaType.APPLICATION_JSON));
  private static final int TIMEOUT = 10000;

  @Rule public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void get_file_dto_forbidden() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .get(ENDPOINT + "/" + TEST_FILE_NAME)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  public void get_file_dto_notFound() {
    given()
        .accept(MEDIA_TYPE)
        .auth()
        .oauth2(getEntrepreneurJwt())
        .when()
        .get(ENDPOINT + "/" + TEST_FILE_NAME)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void get_file_dto_ok() throws IOException, InterruptedException {
    File file = createFile(TEST_FILE_NAME);
    String fileName = uploadFile(file);

    Thread.sleep(TIMEOUT);

    given()
        .accept(MEDIA_TYPE)
        .auth()
        .oauth2(getEntrepreneurJwt())
        .when()
        .get(ENDPOINT + "/" + fileName)
        .then()
        .statusCode(HttpStatus.OK.value())
        .assertThat()
        .body("type", equalTo(TEST_FILE_TYPE))
        .body("file", notNullValue());
  }

  @Test
  public void get_file_public_notFound() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .get(ENDPOINT + "/public/" + TEST_FILE_NAME)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void get_file_public_ok() {
    // TODO : implement after campaign topic upload image
  }

  @Test
  public void upload_file_no_extension() throws IOException {
    given()
        .accept(MediaType.TEXT_PLAIN_VALUE)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .when()
        .multiPart(createFile(TEST_FILE_NAME.split("\\.")[0]))
        .post(ENDPOINT)
        .then()
        .statusCode(HttpStatus.NOT_ACCEPTABLE.value());
  }

  @Test
  public void upload_file_ok() throws IOException {
    given()
        .accept(MediaType.TEXT_PLAIN_VALUE)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        .when()
        .multiPart(createFile(TEST_FILE_NAME))
        .post(ENDPOINT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .contentType(MediaType.TEXT_PLAIN_VALUE);
  }

  @Test
  public void delete_file_forbidden() {
    given()
        .accept(MEDIA_TYPE)
        .when()
        .delete(ENDPOINT + "/" + TEST_FILE_NAME)
        .then()
        .statusCode(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  public void delete_file_ok() throws IOException, InterruptedException {
    File file = createFile(TEST_FILE_NAME);
    String fileName = uploadFile(file);

    Thread.sleep(TIMEOUT);

    given()
        .auth()
        .oauth2(getEntrepreneurJwt())
        .accept(MEDIA_TYPE)
        .when()
        .delete(ENDPOINT + "/" + fileName)
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  private File createFile(String fileName) throws IOException {
    File file = folder.newFile(fileName);
    IOUtils.write(TEST_FILE_BYTES, new FileOutputStream(file));
    return file;
  }
}
