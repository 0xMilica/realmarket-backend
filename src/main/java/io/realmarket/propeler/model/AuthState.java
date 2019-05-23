package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.EAuthState;
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
    name = "auth_state",
    indexes = {
      @Index(columnList = "name", unique = true, name = "auth_state_uk_on_name"),
    })
public class AuthState {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_STATE_SEQ")
  @SequenceGenerator(name = "AUTH_STATE_SEQ", sequenceName = "AUTH_STATE_SEQ", allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private EAuthState name;
}
