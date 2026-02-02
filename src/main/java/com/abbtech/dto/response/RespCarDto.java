package com.abbtech.dto.response;

public record RespCarDto(
        Integer id,
        RespModelDto model,
        String vin,
        String registrationNumber,
        Integer mileageKm,
        Integer productionYear) {
}
