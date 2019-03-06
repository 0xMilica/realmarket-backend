package io.realmarket.propeler.api.annotations;

import io.realmarket.propeler.api.annotations.validators.UrlFriendlyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlFriendlyValidator.class)
public @interface UrlFriendly {

  boolean isSpaceAllowed() default false;

  String message() default "Must be url friendly string," + " found: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
