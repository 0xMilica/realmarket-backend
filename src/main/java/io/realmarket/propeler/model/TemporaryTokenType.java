package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.ETemporaryTokenType;
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
    name = "temporary_token_type",
    indexes = {
      @Index(columnList = "name", unique = true, name = "temporary_token_type_uk_on_name"),
    })
public class TemporaryTokenType {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMPORARY_TOKEN_TYPE_SEQ")
  @SequenceGenerator(
      name = "TEMPORARY_TOKEN_TYPE_SEQ",
      sequenceName = "TEMPORARY_TOKEN_TYPE_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ETemporaryTokenType name;
}
