// com.vehiclerental.services.CustomUserDetailsService
package com.vehiclerental.services;

import com.vehiclerental.models.User;
import com.vehiclerental.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // uses the fetch-join method you just added
        User u = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(
                        u.getRoles().stream()
                                .map(Enum::name)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toSet())
                )
                .accountLocked(!u.isEnabled())
                .disabled(!u.isEnabled())
                .build();
    }
}
