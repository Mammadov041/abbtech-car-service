package com.abbtech.dto.response;

import com.abbtech.logging.LogIgnore;

import java.util.List;

public record RespBrandDto(
        Integer id,
        String name,
        String country,
        @LogIgnore
        Integer foundedYear,
        List<RespModelDto> models
) {
}

