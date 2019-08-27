package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Notification;
import io.realmarket.propeler.model.enums.NotificationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(description = "NotificationDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

  @ApiModelProperty(value = "Notification id")
  private Long id;

  @ApiModelProperty(value = "Notification is seen")
  private Boolean seen;

  @ApiModelProperty(value = "Notification title")
  private String title;

  @ApiModelProperty(value = "Notification content")
  private String content;

  @ApiModelProperty(value = "Notification sender first name")
  private String senderFirstName;

  @ApiModelProperty(value = "Notification sender last name")
  private String senderLastName;

  @ApiModelProperty(value = "Notification recipient id")
  private Long recipientId;

  @ApiModelProperty(value = "Notification date")
  private Date date;

  @ApiModelProperty(value = "Notification type")
  private String type;

  public NotificationDto(Notification notification) {
    this.content = notification.getContent();
    this.title = notification.getTitle();
    this.seen = notification.getSeen();
    this.id = notification.getId();
    this.senderFirstName = notification.getSender().getPerson().getFirstName();
    this.senderLastName = notification.getSender().getPerson().getLastName();
    this.recipientId = notification.getRecipient().getId();
    this.date = notification.getDate();
  }

  public NotificationDto(Notification notification, NotificationType type) {
    this.content = notification.getContent();
    this.title = notification.getTitle();
    this.seen = notification.getSeen();
    this.id = notification.getId();
    this.senderFirstName = notification.getSender().getPerson().getFirstName();
    this.senderLastName = notification.getSender().getPerson().getLastName();
    this.recipientId = notification.getRecipient().getId();
    this.date = notification.getDate();
    this.type = type.toString();
  }
}
