package io.realmarket.propeler.model;

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
@Table(name = "company_category")
public class CompanyCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_CATEGORY_SEQ")
  @SequenceGenerator(
      name = "COMPANY_CATEGORY_SEQ",
      sequenceName = "COMPANY_CATEGORY_SEQ",
      allocationSize = 1)
  private Long id;

  private String name;
}
