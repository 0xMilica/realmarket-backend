package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class PlatformSettings {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLATFORM_SETTINGS_SEQ")
  @SequenceGenerator(name = "PLATFORM_SETTINGS_SEQ", sequenceName = "PLATFORM_SETTINGS_SEQ", allocationSize = 1)
  private Long id;

  private Long platformMinimalInvestment;

  private Instant validFrom;

  private Instant validTo;

  @UpdateTimestamp
  private Instant lastUpdated;

  @CreationTimestamp
  private Instant created;
}
