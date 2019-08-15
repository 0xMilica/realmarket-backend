package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.NotificationDto;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/notifications")
public interface NotificationController {

  @ApiOperation(
      value = "Get all user notifications",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "page",
        value = "Number of page to be returned",
        defaultValue = "0",
        dataType = "Integer",
        paramType = "query"),
    @ApiImplicitParam(
        name = "size",
        value = "Page size (number of items to be returned)",
        defaultValue = "10",
        dataType = "Integer",
        paramType = "query"),
    @ApiImplicitParam(
        name = "filter",
        value = "State of notifications",
        allowableValues = "true, false",
        dataType = "Boolean",
        paramType = "query")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "All user notifications successfully retrieved."),
    @ApiResponse(code = 400, message = "All user notifications are not retrieved")
  })
  ResponseEntity<Page<NotificationDto>> getAllNotifications(Pageable pageable, Boolean filter);

  @ApiOperation(
      value = "Get number of unseen notifications",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Get number of unseen notifications successfully retrieved."),
  })
  ResponseEntity<Long> getNumberUnseenNotifications();

  @ApiOperation(
      value = "Change notification status",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully changed notification status."),
    @ApiResponse(code = 404, message = "Notification with provided id not found."),
  })
  ResponseEntity<Void> changeNotificationSeenStatus(Long id);
}
