package io.realmarket.propeler.service.exception.util;

public interface ExceptionMessages {

  String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials provided";

  String USERNAME_ALREADY_EXISTS = "Person with the provided username already exists!";

  String USERNAME_DOES_NOT_EXISTS = "Person with provided username does not exists.";

  String EMAIL_DOES_NOT_EXIST = "Person with provided e-mail address does not exist!";

  String PERSON_ID_DOES_NOT_EXISTS = "Person with provided identifier does not exists.";

  String INVALID_REQUEST = "Invalid request!";

  String EMAIL_SENDING_EXCEPTION = "Email Sending exception: ";

  String INVALID_TOKEN_PROVIDED = "Invalid token provided";

  String INVALID_TOKEN_TYPE = "Invalid token type";

  String TOTP_CODE_NOT_PROVIDED = "TOTP code not provided";

  String INVALID_TOTP_CODE_PROVIDED = "Invalid TOTP code provided";

  String COULD_NOT_GENERATE_TOKEN = "Could not generate token";

  String PROFILE_PICTURE_DOES_NOT_EXIST = "Profile picture does not exist!";

  String IMAGE_DOES_NOT_EXIST = "Image does not exist!";

  String FORBIDDEN_OPERATION_EXCEPTION = "Not allowed operation";

  String AUTHORIZATION_ACTION_NOT_FOUND = "Authorization action not found";

  String CAMPAIGN_NOT_FOUND = "Campaign with the provided name not found!";

  String CAMPAIGN_INVESTOR_NOT_FOUND = "Campaign investor with the provided id not found!";

  String USER_IS_NOT_OWNER_OF_CAMPAIGN = "Caller is not owner of campaign.";

  String TEAM_MEMBER_NOT_FOUND = "Campaign teem member with the provided id not found!";

  String CAMPAIGN_NAME_ALREADY_EXISTS = "Campaign with the provided name already exists!";

  String NOT_COMPANY_OWNER = "User is not an owner of a company!";

  String NOT_CAMPAIGN_COMPANY_OWNER = "User is not an owner of a campaign's company!";

  String FILE_NOT_EXISTS = "File with provided URL does not exist!";
}
