package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "Notification")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQ")
  @SequenceGenerator(
      name = "NOTIFICATION_SEQ",
      sequenceName = "NOTIFICATION_SEQ",
      allocationSize = 1)
  private Long id;

  private Date date;

  @Builder.Default private Boolean seen = false;

  @JoinColumn(name = "senderId", foreignKey = @ForeignKey(name = "notification_fk_on_sender"))
  @ManyToOne
  private Auth sender;

  @JoinColumn(name = "recipientId", foreignKey = @ForeignKey(name = "notification_fk_on_recipient"))
  @NotNull
  @ManyToOne
  private Auth recipient;

  private String content;

  private String title;

  @Builder.Default private Boolean active = true;
}
