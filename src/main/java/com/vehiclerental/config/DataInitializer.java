package com.vehiclerental.config;

import com.vehiclerental.models.User;
import com.vehiclerental.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            Map<String, User.Role> seeds = Map.of(
                    "finance",   User.Role.ROLE_FINANCE_MANAGER,
                    "driver",    User.Role.ROLE_DRIVER,
                    "admin",     User.Role.ROLE_ADMIN,
                    "staff",     User.Role.ROLE_STAFF,
                    "customer",  User.Role.ROLE_CUSTOMER
            );

            for (Map.Entry<String, User.Role> e : seeds.entrySet()) {
                String uname = e.getKey();
                if (!userRepository.existsByUsername(uname)) {
                    User u = new User();
                    u.setUsername(uname);
                    u.setEmail(uname + "@system.com");
                    u.setPassword(passwordEncoder.encode(uname + "123"));
                    u.setFullName(Character.toUpperCase(uname.charAt(0)) + uname.substring(1) + " User");
                    u.setRoles(Collections.singleton(e.getValue()));
                    u.setEnabled(true);
                    u.setFirstLogin(false);
                    userRepository.save(u);
                }
            }
        };
    }
}
