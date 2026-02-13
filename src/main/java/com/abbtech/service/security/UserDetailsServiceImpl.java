package com.abbtech.service.security;

import com.abbtech.exception.AuthErrorEnum;
import com.abbtech.exception.AuthException;
import com.abbtech.model.AppUser;
import com.abbtech.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(AuthErrorEnum.USER_NOT_EXISTS));

        if (!user.getActive()) {
            throw new DisabledException("User inactive");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Roles
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            // Permissions
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getPermissionCode())
                );
            });
        });

        return new User(user.getUsername(), user.getPasswordHash(), authorities);
    }
}