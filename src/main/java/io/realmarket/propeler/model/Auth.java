package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.model.enums.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Auth")
@Table(indexes = @Index(columnList = "username", unique = true, name = "auth_uk_on_username"))
@TypeDef(name = "euserrole", typeClass = PostgreSQLEnumType.class)
public class Auth {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_SEQ")
  @SequenceGenerator(name = "AUTH_SEQ", sequenceName = "AUTH_SEQ", allocationSize = 1)
  private Long id;

  private String username;
  private String password;

  @Column(columnDefinition = "euserrole")
  @Type(type = "euserrole")
  @Enumerated(EnumType.STRING)
  private EUserRole userRole;

  private Boolean active;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "personId", foreignKey = @ForeignKey(name = "auth_fk1_on_person"))
  private Person person;

  public Auth(Long id) {
    this.id = id;
  }
}
