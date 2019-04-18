package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.FileDto;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Api("/files")
public interface FileController {
  @ApiOperation(
      value = "Get file with provided file name",
      httpMethod = "GET",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "fileName",
          value = "Name of the file",
          required = true,
          paramType = "path"))
  @ApiResponses({
    @ApiResponse(code = 200, message = "File with the provided name successfully retrieved."),
    @ApiResponse(code = 400, message = "File with the provided name could not be retrieved."),
    @ApiResponse(code = 404, message = "File with the provided name does not exist.")
  })
  ResponseEntity<FileDto> getFile(String fileName);

  @ApiOperation(
      value = "Get publicly accessible file with provided file name",
      httpMethod = "GET",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "fileName",
          value = "Name of the file",
          required = true,
          paramType = "path"))
  @ApiResponses({
    @ApiResponse(code = 200, message = "File with the provided name successfully retrieved."),
    @ApiResponse(code = 400, message = "File with the provided name could not be retrieved."),
    @ApiResponse(code = 404, message = "File with the provided name does not exist.")
  })
  ResponseEntity<byte[]> getPublicFile(String fileName);

  @ApiOperation(
      value = "Upload file",
      httpMethod = "POST",
      consumes = "multipart/form-data",
      produces = MediaType.TEXT_PLAIN_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "file",
          dataType = "__file",
          value = "File to be uploaded",
          paramType = "form",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 200, message = "File successfully uploaded."),
    @ApiResponse(code = 400, message = "File cannot be saved.")
  })
  ResponseEntity<String> uploadFile(MultipartFile file);

  @ApiOperation(value = "Delete file with provided file name", httpMethod = "DELETE")
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "fileName",
          value = "Name of the file to be deleted",
          paramType = "path",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 200, message = "File with the provided name successfully deleted."),
    @ApiResponse(code = 400, message = "File with the provided name could not be deleted."),
    @ApiResponse(code = 404, message = "File with the provided name does not exist.")
  })
  ResponseEntity<Void> deleteFile(String fileName);
}
