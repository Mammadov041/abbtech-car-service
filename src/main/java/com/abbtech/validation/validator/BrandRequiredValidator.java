package com.abbtech.validation.validator;

import com.abbtech.dto.request.ReqModelDto;
import com.abbtech.validation.annotation.BrandRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BrandRequiredValidator
        implements ConstraintValidator<BrandRequired, ReqModelDto> {

    @Override
    public boolean isValid(ReqModelDto dto, ConstraintValidatorContext context) {

        if (dto == null) return true;

        boolean hasBrandId = dto.brandId() != null;
        boolean hasBrand = dto.brand() != null;

        // XOR logic: exactly one must be true
        if (hasBrandId ^ hasBrand) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Provide either brandId or brand, not both or neither"
        ).addConstraintViolation();

        return false;
    }
}
