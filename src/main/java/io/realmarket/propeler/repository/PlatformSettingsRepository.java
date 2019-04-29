package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.PlatformSettings;
import io.realmarket.propeler.model.enums.EAuthorizedActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PlatformSettingsRepository extends JpaRepository<PlatformSettings, Long> {
    @Query(
            "Select platformSettings FROM PlatformSettings platformSettings WHERE platformSettings.validFrom < CURRENT_TIMESTAMP  and platformSettings.validUntil> CURRENT_TIMESTAMP ")
    Optional<PlatformSettings> findCurrentSettings();
}
