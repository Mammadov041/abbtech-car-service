package com.abbtech.dto.request.security;

import com.abbtech.logging.LogIgnore;

public record LoginRequestDto(
        String username,
        @LogIgnore
        String password
) {}

