package com.vehiclerental.services;

import com.vehiclerental.models.User;
import com.vehiclerental.models.User.Role;
import com.vehiclerental.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final UserRepository userRepository;

    public DriverServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listDrivers() {
        // Users having ROLE_DRIVER in their roles set
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles() != null && u.getRoles().contains(Role.ROLE_DRIVER))
                .toList();
    }
}
