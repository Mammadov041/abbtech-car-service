package com.abbtech.dto.request;

import com.abbtech.validation.annotation.BrandRequired;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@BrandRequired
public record ReqModelDto(
        Integer brandId,
        @Valid
        ReqBrandDto brand,
        @NotBlank(message = "model name can not be blank")
        @Length(max = 12,message = "maximum length of the category is 12 and minimum is 5",min = 5)
        String name,
        @NotBlank(message = "model category can not be blank")
        @Length(max = 15,message = "maximum length of the category is 15 and minimum is 5",min = 5)
        String category,
        @Min(value = 1900,message = "minimum yearFrom can be 1900")
        @Max(value = 2100,message = "maximum yearFrom can be 2100")
        Integer yearFrom,
        @Min(value = 2100,message = "minimum yearTo can be 2100")
        @Max(value = 3000,message = "maximum yearTo can be 3000")
        Integer yearTo,
        @Valid
        List<ReqCarDto> cars) {

}

