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
@Entity(name = "JWT")
@Table(indexes = @Index(columnList = "value", unique = true, name = "jwt_uk_on_value"))
public class JWT {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JWT_SEQ")
  @SequenceGenerator(name = "JWT_SEQ", sequenceName = "JWT_SEQ", allocationSize = 1)
  private Long id;

  private String value;

  private Date expirationTime;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "token_fk1_on_auth"))
  @ManyToOne
  private Auth auth;
}
