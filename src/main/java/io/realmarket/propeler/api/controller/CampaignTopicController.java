package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.CampaignTopicDto;
import io.realmarket.propeler.api.dto.FilenameDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "Campaign topics")
public interface CampaignTopicController {

  @ApiOperation(
      value = "Create new topic",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Campaign topic successfully created."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 409, message = "Campaign topic with the provided type already exists.")
  })
  ResponseEntity<String> createCampaignTopic(
      String campaignName, String type, CampaignTopicDto campaignTopicDto);

  @ApiOperation(
      value = "Upload new topic image",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Campaign topic image successfully uploaded."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<FilenameDto> uploadCampaignTopicImage(
      String campaignName, String topicType, MultipartFile picture);

  @ApiOperation(
      value = "Get campaign topic content",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign successfully retrieved."),
    @ApiResponse(code = 404, message = "Campaign does not exists.")
  })
  ResponseEntity<CampaignTopicDto> getCampaignTopic(String campaignName, String topicType);

  @ApiOperation(
      value = "Update campaign topic content",
      httpMethod = "PUT",
      consumes = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Campaign topic successfully updated."),
    @ApiResponse(code = 404, message = "Campaign or campaign topic does does not exists.")
  })
  ResponseEntity updateCampaignTopic(
      @PathVariable String campaignName,
      @PathVariable String topicType,
      @Valid @RequestBody CampaignTopicDto campaignTopicDto);
}
