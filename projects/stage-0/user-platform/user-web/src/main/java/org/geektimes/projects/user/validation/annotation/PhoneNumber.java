package org.geektimes.projects.user.validation.annotation;

import org.geektimes.projects.user.validation.validatation.PhoneNumberAnnotationValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {PhoneNumberAnnotationValidator.class})
public @interface PhoneNumber {

    String message() default "{org.geektimes.projects.user.validation.annotation.PhoneNumber.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
