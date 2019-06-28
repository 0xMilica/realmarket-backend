package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.AuthorizedActionTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "authorized_action_type",
    indexes = {
      @Index(columnList = "name", unique = true, name = "authorized_action_type_uk_on_name"),
    })
public class AuthorizedActionType {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHORIZED_ACTION_TYPE_SEQ")
  @SequenceGenerator(
      name = "AUTHORIZED_ACTION_TYPE_SEQ",
      sequenceName = "AUTHORIZED_ACTION_TYPE_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private AuthorizedActionTypeName name;
}
