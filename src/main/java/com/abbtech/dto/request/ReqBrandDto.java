package com.abbtech.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record ReqBrandDto(
        @NotBlank(message = "name can not be empty or null") String name,
        String country,
        @Positive(message = "founded year must be positive")
        @Min(1900)
        @Max(2100)
        Integer foundedYear,
        @Size(min = 1, message = "at least one model is required,max=2", max = 2)
        @Valid List<ReqModelDto> models
) {
}
