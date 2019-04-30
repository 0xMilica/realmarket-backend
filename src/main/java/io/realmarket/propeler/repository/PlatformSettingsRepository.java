package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.PlatformSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformSettingsRepository extends JpaRepository<PlatformSettings, Long> {

  Optional<PlatformSettings> findTopBy();
}
