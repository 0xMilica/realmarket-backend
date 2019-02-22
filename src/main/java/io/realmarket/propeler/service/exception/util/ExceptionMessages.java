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

  String FORBIDDEN_OPERATION_EXCEPTION = "Not allowed operation";

  String AUTHORIZATION_ACTION_NOT_FOUND = "Authorization action not found";
}
