package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.RequestStateName;
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
    name = "request_state",
    indexes = {@Index(columnList = "name", unique = true, name = "request_state_uk_on_name")})
public class RequestState {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_STATE_SEQ")
  @SequenceGenerator(
      name = "REQUEST_STATE_SEQ",
      sequenceName = "REQUEST_STATE_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private RequestStateName name;
}
