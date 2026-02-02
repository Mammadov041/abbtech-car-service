package com.abbtech.validation.validator;

import com.abbtech.dto.request.ReqCarDto;
import com.abbtech.validation.annotation.ModelRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ModelRequiredValidator
    implements ConstraintValidator<ModelRequired, ReqCarDto> {

    @Override
    public boolean isValid(ReqCarDto reqCarDto, ConstraintValidatorContext constraintValidatorContext) {

        if (reqCarDto == null) return true;

        boolean hasBrandId = reqCarDto.modelId() != null;
        boolean hasBrand = reqCarDto.model() != null;

        // XOR logic: exactly one must be true
        if (hasBrandId ^ hasBrand) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                "Provide either modelId or model, not both or neither"
        ).addConstraintViolation();

        return false;
    }
}
