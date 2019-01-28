package io.realmarket.propeler.api.annotations;

import io.realmarket.propeler.api.annotations.validators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface Email {

  String message() default "Must be a valid email address," + " found: ${validatedValue}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
