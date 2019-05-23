package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.EUserRole;
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
    name = "user_role",
    indexes = {
      @Index(columnList = "name", unique = true, name = "user_role_uk_on_name"),
    })
public class UserRole {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ROLE_SEQ")
  @SequenceGenerator(name = "USER_ROLE_SEQ", sequenceName = "USER_ROLE_SEQ", allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private EUserRole name;
}
