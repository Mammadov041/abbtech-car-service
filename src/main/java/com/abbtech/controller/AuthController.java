package com.abbtech.controller;

import com.abbtech.dto.request.security.LoginRequestDto;
import com.abbtech.dto.request.security.RefreshRequestDto;
import com.abbtech.dto.request.security.RegisterRequestDto;
import com.abbtech.dto.response.security.TokenResponseDto;
import com.abbtech.exception.AuthErrorEnum;
import com.abbtech.exception.AuthException;
import com.abbtech.service.security.JwtService;
import com.abbtech.service.security.RegisterService;
import com.abbtech.service.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtService jwt;
    private final RegisterService registerService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto req) {
        Authentication auth =
                manager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        UserDetails user = (UserDetails) auth.getPrincipal();
        return new TokenResponseDto(jwt.accessToken(user), jwt.refreshToken(user));
    }


    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDto req) {
        registerService.register(req);
    }

    @PostMapping("/refresh")
    public TokenResponseDto refresh(@RequestBody RefreshRequestDto req) {
        Claims claims = jwt.parse(req.refreshToken());
        if (!"refresh".equals(claims.get("type"))) {
            throw new AuthException(AuthErrorEnum.INVALID_TOKEN);
        }
        String username = claims.getSubject();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return new TokenResponseDto(jwt.accessToken(user), jwt.refreshToken(user));
    }
}
