package com.abbtech.dto.request;

import com.abbtech.validation.annotation.ModelRequired;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@ModelRequired
public record ReqCarDto(
        Integer modelId,
        @Valid
        ReqModelDto model,
        @NotEmpty(message = "vin number can not be empty")
        String vin,
        String registrationNumber,
        @Min(value = 0,message = "mileage must be at least 0 km")
        @Positive(message = "millage must be a positive number")
        Integer mileageKm,
        @Positive(message = "productionYear must be a positive number")
        Integer productionYear
) {
}
