package com.abbtech.dto.response;

import java.util.List;

public record RespBrandDto(
        Integer id,
        String name,
        String country,
        Integer foundedYear,
        List<RespModelDto> models
) {
}

