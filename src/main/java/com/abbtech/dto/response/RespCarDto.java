package com.abbtech.dto.response;

import com.abbtech.logging.LogIgnore;

public record RespCarDto(
        Integer id,
        RespModelDto model,
        @LogIgnore
        String vin,
        String registrationNumber,
        Integer mileageKm,
        Integer productionYear) {
}
