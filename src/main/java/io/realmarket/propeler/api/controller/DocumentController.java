package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.model.DocumentMetadata;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/{entity}/documents")
public interface DocumentController {

  @ApiOperation(
      value = "Get entity document types",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "entity",
        value = "Name of entity for which document types need to be retrieved",
        dataType = "String",
        paramType = "path",
        allowableValues = "companies, campaigns, fundraisingProposals",
        required = true)
  })
  @ApiResponses({@ApiResponse(code = 200, message = "Successfully retrieved document types.")})
  ResponseEntity<List<DocumentMetadata>> getDocumentTypes(String entity);
}
