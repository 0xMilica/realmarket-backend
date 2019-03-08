package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.FileController;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.service.FileService;
import io.realmarket.propeler.service.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/files")
@Slf4j
public class FileControllerImpl implements FileController {

  private final FileService fileService;

  @Autowired
  public FileControllerImpl(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping(value = "/{fileName:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FileDto> getFile(@PathVariable String fileName) {
    return ResponseEntity.ok(fileService.getFile(fileName));
  }

  @PostMapping(consumes = "multipart/form-data", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> uploadFile(@NotNull MultipartFile file) {
    FileUtils.getExtensionOrThrowException(file);
    return ResponseEntity.ok(fileService.uploadFile(file));
  }

  @DeleteMapping(value = "/{fileName:.+}")
  public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
    this.fileService.deleteFile(fileName);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
