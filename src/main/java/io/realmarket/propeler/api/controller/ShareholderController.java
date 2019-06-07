package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.ShareholderDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "Shareholders")
public interface ShareholderController {

  @ApiOperation(
      value = "Create new shareholder",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Shareholder successfully created."),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity<ShareholderDto> createShareholder(ShareholderDto shareholderDto);

  @ApiOperation(
      value = "Change order of showing shareholders",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Order of shareholders changed."),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity patchShareholderOrder(List<Long> shareholderOrder);

  @ApiOperation(
      value = "Get all shareholders",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Shareholders successfully retrieved."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<List<ShareholderDto>> getShareholders();

  @ApiOperation(
      value = "Get all shareholders",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Shareholders successfully retrieved."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<List<ShareholderDto>> getShareholders(Long companyId);

  @ApiOperation(
      value = "Patch shareholder",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Shareholder successfully created."),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity patchShareholder(Long shareholderId, ShareholderDto shareholderDto);

  @ApiOperation(
      value = "Delete shareholder",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 204, message = "Shareholder removed"),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity deleteShareholder(Long shareholderId);

  @ApiOperation(
      value = "Upload shareholder picture",
      httpMethod = "POST",
      consumes = "multipart/form-data",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "Shareholder picture",
          dataType = "file",
          value = "Shareholder to be uploaded",
          paramType = "form",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 201, message = "Picture successfully uploaded."),
    @ApiResponse(code = 400, message = "Picture cannot be saved.")
  })
  ResponseEntity uploadShareholderPicture(Long shareholderId, MultipartFile picture);

  @ApiOperation(
      value = "Download shareholder picture",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture retrieved successfully."),
    @ApiResponse(code = 400, message = "Picture cannot be found.")
  })
  ResponseEntity<FileDto> downloadShareholderPicture(Long shareholderId);

  @ApiOperation(
      value = "Download shareholder picture",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture retrieved successfully."),
    @ApiResponse(code = 400, message = "Picture cannot be found.")
  })
  ResponseEntity<FileDto> downloadShareholderPicture(Long companyId, Long shareholderId);

  @ApiOperation(
      value = "Delete shareholder picture",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture successfully deleted."),
    @ApiResponse(code = 500, message = "Internal server error.")
  })
  ResponseEntity deleteShareholderPicture(Long shareholderId);
}
