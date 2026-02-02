package com.abbtech.validation.annotation;

import com.abbtech.validation.validator.BrandRequiredValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BrandRequiredValidator.class)
public @interface BrandRequired {

    String message() default "Either brandId or brand must be provided (but not both)";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
