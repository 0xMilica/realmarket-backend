package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.impl.FileServiceImpl;
import io.realmarket.propeler.unit.util.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class FileServiceImplTest {

  @Mock private CloudObjectStorageService cloudService;
  @InjectMocks private FileServiceImpl fileService;

  @Test
  public void GetFile_Should_Return_FileDto() {
    when(cloudService.download(FileUtils.TEST_FILE_NAME)).thenReturn(FileUtils.TEST_FILE_BYTES);

    FileDto fileDto = fileService.getFile(FileUtils.TEST_FILE_NAME);

    verify(cloudService, times(1)).download(FileUtils.TEST_FILE_NAME);
    assertEquals(FileUtils.TEST_FILE_BASE64_ENCODED, fileDto.getFile());
    assertEquals(FileUtils.TEST_FILE_TYPE, fileDto.getType());
  }

  @Test
  public void UploadFile_Should_Return_FileName() {
    fileService.uploadFile(FileUtils.MOCK_FILE_VALID);

    verify(cloudService, times(1)).upload(anyString(), any(), anyInt());
  }
}
