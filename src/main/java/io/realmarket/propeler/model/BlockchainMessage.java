package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "blockchain_message")
public class BlockchainMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BLOCKCHAIN_MESSAGE_SEQ")
  @SequenceGenerator(
      name = "BLOCKCHAIN_MESSAGE_SEQ",
      sequenceName = "BLOCKCHAIN_MESSAGE_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(length = 4096)
  private String messageDetails;
}
