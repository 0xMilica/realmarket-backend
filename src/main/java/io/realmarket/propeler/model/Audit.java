package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "audit")
public class Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_SEQ")
  @SequenceGenerator(name = "AUDIT_SEQ", sequenceName = "AUDIT_SEQ", allocationSize = 1)
  private Long id;

  @NotNull @ManyToOne private Auth auditorAuth;

  @NotNull @ManyToOne private Campaign campaign;

  @NotNull @ManyToOne private RequestState requestState;

  @Column(length = 10000)
  private String content;
}
