package com.gadgetmarket.validation.annotation;

import com.gadgetmarket.validation.validator.MatchPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatchPasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchPassword {
    String message() default "passwords don't match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}