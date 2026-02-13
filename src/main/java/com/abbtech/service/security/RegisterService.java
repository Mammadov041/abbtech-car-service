package com.abbtech.service.security;

import com.abbtech.dto.request.security.RegisterRequestDto;
import com.abbtech.exception.AuthErrorEnum;
import com.abbtech.exception.AuthException;
import com.abbtech.model.AppUser;
import com.abbtech.model.Role;
import com.abbtech.repository.security.RoleRepository;
import com.abbtech.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @Transactional
    public void register(RegisterRequestDto req) {

        if (userRepo.findByUsername(req.username()).isPresent()) {
            throw new AuthException(AuthErrorEnum.USERNAME_EXISTS);
        }

        Role userRole = roleRepo.findByName("USER").orElseThrow();

        AppUser user = new AppUser();
        user.setUsername(req.username());
        user.setFullName(req.fullName());
        user.setEmail(req.email());

        user.setPasswordHash(encoder.encode(req.password()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(Set.of(userRole));
        userRepo.save(user);
    }
}

