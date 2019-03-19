package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.CompanyDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/companies")
public interface CompanyController {

  @ApiOperation(
      value = "Create new company",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Company successfully created."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity createCompany(CompanyDto companyDto);

  @ApiOperation(
      value = "Get company's basic info",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(name = "id", value = "Company's id", required = true, dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Company successfully retrieved."),
    @ApiResponse(code = 404, message = "Company does not exists.")
  })
  ResponseEntity getCompany(Long companyId);

  @ApiOperation(
      value = "Upload company logo",
      httpMethod = "POST",
      consumes = "multipart/form-data",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "Logo picture",
          dataType = "file",
          value = "Logo to be uploaded",
          paramType = "form",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 201, message = "Logo successfully uploaded."),
    @ApiResponse(code = 400, message = "Logo cannot be saved.")
  })
  ResponseEntity uploadLogo(Long companyId, MultipartFile picture);

  @ApiOperation(
      value = "Download company logo",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Logo retrieved successfully."),
    @ApiResponse(code = 400, message = "Logo cannot be found.")
  })
  ResponseEntity<FileDto> downloadLogo(Long companyId);

  @ApiOperation(
      value = "Delete company logo",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Logo successfully deleted."),
    @ApiResponse(code = 500, message = "Internal server error.")
  })
  ResponseEntity deleteLogo(Long companyId);

  @ApiOperation(
      value = "Upload company featured image",
      httpMethod = "POST",
      consumes = "multipart/form-data",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "featured image",
          dataType = "file",
          value = "Featured image to be uploaded",
          paramType = "form",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 201, message = "Featured image successfully uploaded."),
    @ApiResponse(code = 400, message = "Featured image cannot be saved.")
  })
  ResponseEntity uploadFeaturedImage(Long companyId, MultipartFile picture);

  @ApiOperation(
      value = "Download company featured image",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Featured image retrieved successfully."),
    @ApiResponse(code = 400, message = "Featured image cannot be found.")
  })
  ResponseEntity<FileDto> downloadFeaturedImage(Long companyId);

  @ApiOperation(
      value = "Delete company featured image",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 204, message = "Featured image successfully deleted."),
    @ApiResponse(code = 500, message = "Internal server error.")
  })
  ResponseEntity deleteFeaturedImage(Long companyId);
}
