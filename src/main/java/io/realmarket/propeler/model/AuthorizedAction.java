package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "AuthorizedAction")
@Table(
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"typeId", "authId"},
          name = "authorized_action_uk_on_type_and_auth")
    })
public class AuthorizedAction {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHORIZED_ACTION_SEQ")
  @SequenceGenerator(
      name = "AUTHORIZED_ACTION_SEQ",
      sequenceName = "AUTHORIZED_ACTION_SEQ",
      allocationSize = 1)
  private Long id;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "authorized_action_on_auth"))
  @ManyToOne
  private Auth auth;

  private String data;

  private Instant expiration;

  @JoinColumn(name = "typeId", foreignKey = @ForeignKey(name = "authorized_action_on_type"))
  @ManyToOne
  private AuthorizedActionType type;
}
