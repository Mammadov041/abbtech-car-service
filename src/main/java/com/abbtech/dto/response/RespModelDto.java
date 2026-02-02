package com.abbtech.dto.response;

import java.util.List;

public record RespModelDto(
        Integer id,
        RespBrandDto brand,
        List<RespCarDto> cars,
        String name,
        String category,
        Integer yearFrom,
        Integer yearTo
        ) {}
