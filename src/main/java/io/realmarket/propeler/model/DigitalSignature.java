package io.realmarket.propeler.model;

import io.realmarket.propeler.api.dto.DigitalSignaturePrivateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "digital_signature")
public class DigitalSignature {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIGITAL_SIGNATURE_SEQ")
  @SequenceGenerator(
      name = "DIGITAL_SIGNATURE_SEQ",
      sequenceName = "DIGITAL_SIGNATURE_SEQ",
      allocationSize = 1)
  private Long id;

  private String encryptedPrivateKey;
  private String publicKey;
  private String initialVector;
  private String salt;
  private Integer passLength;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "digital_signature_fk_on_auth"))
  private Auth auth;

  public DigitalSignature(DigitalSignaturePrivateDto digitalSignaturePrivateDto) {
    this.encryptedPrivateKey = digitalSignaturePrivateDto.getEncryptedPrivateKey();
    this.publicKey = digitalSignaturePrivateDto.getPublicKey();
    this.initialVector = digitalSignaturePrivateDto.getInitialVector();
    this.salt = digitalSignaturePrivateDto.getSalt();
    this.passLength = digitalSignaturePrivateDto.getPassLength();
  }
}
