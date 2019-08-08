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
      case ON_DEMAND:
        if (UserRoleName.getInvestorRoleNames().contains(userRoleName)) return true;
        // Access INVESTOR means that ROLE_INDIVIDUAL_INVESTOR and ROLE_COMPANY_INVESTOR as well as
        // ROLE_ADMIN
        // can access this document. because of this break is omitted.
      case PRIVATE:
        if (userRoleName.equals(UserRoleName.ROLE_ADMIN)) return true;
      default:
        return false;
    }
  }

  public static boolean hasReadAccess(
      DocumentAccessLevel accessLevel, UserRoleName userRoleName, boolean hasDocumentsAccess) {
    switch (accessLevel.getName()) {
      case PUBLIC:
        return true;
      case ON_DEMAND:
        if (UserRoleName.getInvestorRoleNames().contains(userRoleName)) {
          return hasDocumentsAccess;
        }
        // Access INVESTOR means that ROLE_INDIVIDUAL_INVESTOR and ROLE_COMPANY_INVESTOR as well as
        // ROLE_ADMIN
        // can access this document. because of this break is omitted.
      case PRIVATE:
        if (userRoleName.equals(UserRoleName.ROLE_ADMIN)) return true;
      default:
        return false;
    }
  }
}
