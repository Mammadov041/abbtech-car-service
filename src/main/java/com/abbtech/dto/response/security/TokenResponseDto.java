package com.abbtech.dto.response.security;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {}
