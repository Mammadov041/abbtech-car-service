package com.abbtech.dto.request.security;

public record RegisterRequestDto(
        String username,
        String fullName,
        String email,
        String password
) {}

