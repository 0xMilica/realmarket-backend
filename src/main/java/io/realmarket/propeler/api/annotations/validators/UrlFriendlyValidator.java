package io.realmarket.propeler.api.annotations.validators;

import io.realmarket.propeler.api.annotations.UrlFriendly;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlFriendlyValidator implements ConstraintValidator<UrlFriendly, String> {
  private boolean isSpaceValid;

  @Override
  public void initialize(UrlFriendly valueToBeValidated) {
    this.isSpaceValid = valueToBeValidated.isSpaceAllowed();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    String regex;

    if (isSpaceValid) {
      regex = "[\\w ]+";
    } else {
      regex = "[\\w]+";
    }

    return value != null && value.matches(regex);
  }
}
