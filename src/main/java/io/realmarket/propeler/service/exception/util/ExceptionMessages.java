package io.realmarket.propeler.service.exception.util;

public interface ExceptionMessages {

  // Person with the provided username already exists.
  String USERNAME_ALREADY_EXISTS = "REG_001";

  // Email Sending exception:
  String EMAIL_SENDING_EXCEPTION = "REG_002";

  // Invalid credentials provided.
  String INVALID_CREDENTIALS_MESSAGE = "LOG_001";

  // Person with provided username does not exist.
  String USERNAME_DOES_NOT_EXIST = "LOG_002";

  // Person with provided e-mail address does not exist.
  String EMAIL_DOES_NOT_EXIST = "LOG_003";

  // Person with provided identifier does not exist.
  String PERSON_ID_DOES_NOT_EXIST = "LOG_004";

  // Invalid token provided.
  String INVALID_TOKEN_PROVIDED = "LOG_005";

  // Invalid token type.
  String INVALID_TOKEN_TYPE = "LOG_006";

  // Invalid TOTP code provided.
  String INVALID_TOTP_CODE_PROVIDED = "LOG_007";

  // Captcha did not validate successfully.
  String INVALID_CAPTCHA = "LOG_008";

  // Maximum number of failed attempts exceeded, try again in half an hour.
  String BLOCKED_CLIENT = "LOG_009";

  // Authorization action not found.
  String AUTHORIZATION_ACTION_NOT_FOUND = "LOG_010";

  // User is not the owner of a company.
  String NOT_COMPANY_OWNER = "COM_001";

  // Company already exists.
  String COMPANY_ALREADY_EXISTS = "COM_002";

  // Company with provided id does not exist.
  String COMPANY_DOES_NOT_EXIST = "COM_003";

  // Entrepreneur does not have a company.
  String ENTREPRENEUR_MISSING_COMPANY = "COM_004";

  // Shareholder with the provided id not found.
  String SHAREHOLDER_NOT_FOUND = "COM_005";

  // Campaign with the provided name not found.
  String CAMPAIGN_NOT_FOUND = "CAM_001";

  // Campaign state with the provided name not found.
  String CAMPAIGN_STATE_NOT_FOUND = "CAM_002";

  // User is not owner of campaign.
  String USER_IS_NOT_OWNER_OF_CAMPAIGN = "CAM_003";

  // Campaign team member with the provided id not found.
  String TEAM_MEMBER_NOT_FOUND = "CAM_004";

  // Campaign with the provided name already exists.
  String CAMPAIGN_NAME_ALREADY_EXISTS = "CAM_005";

  // Campaign topic type with the provided name does not exist.
  String CAMPAIGN_TOPIC_TYPE_DOES_NOT_EXIST = "CAM_006";

  // Active campaign already exists for given company.
  String ACTIVE_CAMPAIGN_EXISTS = "CAM_007";

  // Campaign topic for the provided campaign name and campaign topic type not found.
  String CAMPAIGN_TOPIC_DOES_NOT_EXIST = "CAM_008";

  // Campaign can not be edited at this stage.
  String CAMPAIGN_NOT_EDITABLE = "CAM_009";

  // Campaign is not active.
  String CAMPAIGN_IS_NOT_ACTIVE = "CAM_010";

  // No active campaign.
  String NO_ACTIVE_CAMPAIGN = "CAM_011";

  // Campaign update for provided id not found.
  String CAMPAIGN_UPDATE_NOT_FOUND = "CAM_012";

  // User is not auditor of campaign
  String USER_IS_NOT_AUDITOR_OF_CAMPAIGN = "CAM_013";

  // User is not investor in this campaign.
  String NOT_CAMPAIGN_INVESTOR = "INV_001";

  // Investment state with the provided name not found.
  String INVESTMENT_STATE_NOT_FOUND = "INV_002";

  // Campaign investment must be greater than platform minimum.
  String INVESTMENT_MUST_BE_GREATER_THAN_PLATFORM_MIN = "INV_003";

  // Amount of money must be greater than campaign minimum investment.
  String INVESTMENT_MUST_BE_GREATER_THAN_CAMPAIGN_MIN_INVESTMENT = "INV_004";

  // Amount of money can not be greater than maximum investment.
  String INVESTMENT_CAN_NOT_BE_GREATER_THAN_MAX_INVESTMENT = "INV_005";

  // Percentage of equity can not be greater than campaign maximum equity offered.
  String INVESTMENT_CAN_NOT_BE_GREATER_THAN_CAMPAIGN_MAXIMUM_EQUITY_AVAILABLE = "INV_006";

  // Negative value provided.
  String NEGATIVE_VALUE_EXCEPTION = "INV_007";

  // Investment can be revoked at this stage.
  String INVESTMENT_CAN_BE_REVOKED = "INV_008";

  // Investment can not be revoked at this stage.
  String INVESTMENT_CAN_NOT_BE_REVOKED = "INV_009";

  // Auditing request not found.
  String AUDITING_REQUEST_NOT_FOUND = "AUD_001";

  // User is not auditor of this campaign.
  String NOT_CAMPAIGN_AUDITOR = "AUD_002";

  // User can not be auditor
  String USER_CAN_NOT_BE_AUDITOR = "AUD_003";

  // Pending audit for campaign not found
  String PENDING_AUDIT_NOT_FOUND = "AUD_004";

  // Payment not processed for this investment.
  String PAYMENT_NOT_PROCESSED = "PAY_001";

  // Invalid request.
  String INVALID_REQUEST = "REQ_001";

  // Request state with the provided name not found.
  String REQUEST_STATE_NOT_FOUND = "REQ_002";

  // Profile picture does not exist.
  String PROFILE_PICTURE_DOES_NOT_EXIST = "IMG_001";

  // Image does not exist.
  String IMAGE_DOES_NOT_EXIST = "IMG_002";

  // Not allowed operation.
  String FORBIDDEN_OPERATION_EXCEPTION = "FRB_001";

  // Platform settings can not be found.
  String PLATFORM_SETTINGS_NOT_FOUND = "PTF_001";

  // Invalid country code.
  String INVALID_COUNTRY_CODE = "CTR_001";

  // File with provided URL does not exist.
  String FILE_DOES_NOT_EXIST = "FIL_001";

  // No SHA Algorithm.
  String NO_HASH_ALGORITHM = "SHA_001";

  // User KYC with provided id does not exist.
  String USER_KYC_DOES_NOT_EXIST = "KYC_001";

  // User is not the auditor of user KYC.
  String NOT_USER_KYC_AUDITOR = "KYC_002";

  // User KYC is not assigned yet.
  String USER_KYC_AUDITOR_NOT_ASSIGNED = "KYC_003";

  // User is not owner of this KYC request.
  String USER_IS_NOT_OWNER_OF_KYC = "KYC_004";
}
