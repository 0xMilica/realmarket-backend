package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.service.CloudObjectStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.util.FileUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class FileServiceImplTest {

  @Mock private CloudObjectStorageService cloudService;
  @InjectMocks private FileServiceImpl fileService;

  @Test
  public void GetFile_Should_Return_FileDto() {
    when(cloudService.downloadFileDto(TEST_FILE_NAME)).thenReturn(TEST_FILE_DTO);

    FileDto fileDto = fileService.getFile(TEST_FILE_NAME);

    verify(cloudService, times(1)).downloadFileDto(TEST_FILE_NAME);
    assertEquals(TEST_FILE_DTO, fileDto);
  }

  @Test
  public void UploadFile_Should_Return_FileName() {
    fileService.uploadFile(MOCK_FILE_VALID);

    verify(cloudService, times(1)).upload(anyString(), any(), anyInt());
  }

  @Test
  public void UploadPdfFile_Should_ReturnFileName() {
    byte[] file = mockBytesFile();
    String extension = "pdf";

    fileService.uploadFile(file, extension);

    verify(cloudService, times(1)).upload(anyString(), any(), anyInt());
  }

  @Test
  public void DeleteFile_Should_DeleteFile() {
    fileService.deleteFile(TEST_FILE_NAME);

    verify(cloudService, times(1)).delete(TEST_FILE_NAME);
  }
}
