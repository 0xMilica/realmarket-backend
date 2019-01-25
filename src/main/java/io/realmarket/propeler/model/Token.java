package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Token")
@Table(indexes = @Index(columnList = "jwt", unique = true))
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOKEN_SEQ")
  @SequenceGenerator(name = "TOKEN_SEQ", sequenceName = "TOKEN_SEQ", allocationSize = 1)
  private Long id;
  private String jwt;
  private Date expirationTime;
  @ManyToOne private Auth auth;
}
