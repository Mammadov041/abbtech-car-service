package com.abbtech.validation.annotation;

import com.abbtech.validation.validator.ModelRequiredValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ModelRequiredValidator.class)
public @interface ModelRequired {
    String message() default "Either modelId or model must be provided (but not both)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
