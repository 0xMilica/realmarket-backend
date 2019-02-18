package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmailChangeRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAIL_CHANGE_REQUEST_SEQ")
  @SequenceGenerator(
      name = "EMAIL_CHANGE_REQUEST_SEQ",
      sequenceName = "EMAIL_CHANGE_REQUEST_SEQ",
      allocationSize = 1)
  private Long id;

  private String newEmail;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "personId", foreignKey = @ForeignKey(name = "email_change_req_fk1_on_person"))
  private Person person;

  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "tokenId",
      foreignKey = @ForeignKey(name = "email_change_req_fk1_on_temporary_token"))
  private TemporaryToken temporaryToken;
}
