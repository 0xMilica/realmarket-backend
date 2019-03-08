package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.FileDto;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Base64;

public class FileUtils {

  public static final byte[] TEST_FILE_BYTES_EMPTY = "".getBytes();
  public static final String TEST_FILE_TYPE = "PNG";
  public static final String TEST_FILE_BASE64 = "TEST_FILE_BASE64";

  public static final String TEST_FILE_NAME = "TEST_FILE_NAME." + TEST_FILE_TYPE;
  public static final String TEST_FILE_NAME_2 = "TEST_FILE_NAME_2." + TEST_FILE_TYPE;
  public static final byte[] TEST_FILE_BYTES = "TEST_FILE".getBytes();
  public static final String TEST_FILE_BASE64_ENCODED =
      Base64.getEncoder().encodeToString(FileUtils.TEST_FILE_BYTES);

  public static final FileDto TEST_FILE_DTO = new FileDto(TEST_FILE_TYPE, TEST_FILE_BASE64);

  private static final String TEST_CONTENT_TYPE = "image/PNG";
  public static MockMultipartFile MOCK_FILE_VALID =
      new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_NAME, TEST_CONTENT_TYPE, TEST_FILE_BYTES);

  public static MockMultipartFile MOCK_FILE_EMPTY =
      new MockMultipartFile(
          TEST_FILE_NAME, TEST_FILE_NAME, TEST_CONTENT_TYPE, TEST_FILE_BYTES_EMPTY);

  public static MockMultipartFile MOCK_FILE_NO_EXTENSION =
      new MockMultipartFile(TEST_FILE_NAME, "TEST_FILE_NAME", TEST_CONTENT_TYPE, TEST_FILE_BYTES);
}
