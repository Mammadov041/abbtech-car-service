package com.abbtech.dto.request.security;

import com.abbtech.logging.LogIgnore;

public record RegisterRequestDto(
        String username,
        String fullName,
        @LogIgnore
        String email,
        @LogIgnore
        String password
) {}

