package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.UserRoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "document_access_level",
    indexes = {
      @Index(columnList = "name", unique = true, name = "document_access_level_uk_on_name"),
    })
public class DocumentAccessLevel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_ACCESS_LEVEL_SEQ")
  @SequenceGenerator(
      name = "DOCUMENT_ACCESS_LEVEL_SEQ",
      sequenceName = "DOCUMENT_ACCESS_LEVEL_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private DocumentAccessLevelName name;

  public static boolean hasReadAccess(DocumentAccessLevel accessLevel, UserRoleName userRoleName) {
    switch (accessLevel.getName()) {
      case PUBLIC:
        return true;
      case INVESTORS:
        if (userRoleName.equals(UserRoleName.ROLE_INVESTOR)) return true;
        // Access INVESTOR means that both ROLE_INVESTOR and ROLE_ADMIN can access this document.
        // because of this break is omitted.
      case PLATFORM_ADMINS:
        if (userRoleName.equals(UserRoleName.ROLE_ADMIN)) return true;
      default:
        return false;
    }
  }
}
