package com.vehiclerental.services;

import com.vehiclerental.models.User;
import com.vehiclerental.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ---------- Dashboard counters ----------
    @Override
    public Long getTotalUsers() {
        return userRepository.countTotalUsers();
    }

    @Override
    public Long getActiveUsers() {
        return userRepository.countActiveUsers();
    }

    // ---------- Existence checks ----------
    @Override
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // ---------- Registration / creation ----------
    @Override
    @Transactional
    public void registerCustomer(User user) {
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim().toLowerCase());

        if (usernameExists(user.getUsername())) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        if (emailExists(user.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(User.Role.ROLE_CUSTOMER));
        user.setEnabled(true);
        user.setFirstLogin(false);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createStaffUser(User user, User.Role role) {
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim().toLowerCase());

        if (usernameExists(user.getUsername())) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        if (emailExists(user.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        String raw = (user.getPassword() == null || user.getPassword().isBlank()) ? "default123" : user.getPassword();
        user.setPassword(passwordEncoder.encode(raw));

        user.setRoles(Collections.singleton(role));
        user.setEnabled(true);
        user.setFirstLogin(true);

        userRepository.save(user);
    }

    // ---------- Admin helpers for Manage Users ----------
    @Override
    public Page<User> search(String query, Pageable pageable) {
        return userRepository.search(query, pageable);
    }

    @Override
    @Transactional
    public void createByAdmin(User user, User.Role[] roles) {
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim().toLowerCase());

        if (usernameExists(user.getUsername())) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        if (emailExists(user.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        String raw = (user.getPassword() == null || user.getPassword().isBlank()) ? "default123" : user.getPassword();
        user.setPassword(passwordEncoder.encode(raw));

        Set<User.Role> set = new HashSet<>();
        if (roles != null) {
            Collections.addAll(set, roles);
        } else {
            set.add(User.Role.ROLE_CUSTOMER);
        }
        user.setRoles(set);

        if (!user.isEnabled()) user.setEnabled(true);
        user.setFirstLogin(true);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateByAdmin(Long id, User form, User.Role[] roles) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        if (form.getUsername() != null && !form.getUsername().equals(existing.getUsername())) {
            if (usernameExists(form.getUsername())) {
                throw new DataIntegrityViolationException("Username already exists");
            }
            existing.setUsername(form.getUsername().trim());
        }

        if (form.getEmail() != null && !form.getEmail().equalsIgnoreCase(existing.getEmail())) {
            if (emailExists(form.getEmail())) {
                throw new DataIntegrityViolationException("Email already exists");
            }
            existing.setEmail(form.getEmail().trim().toLowerCase());
        }

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(form.getPassword()));
            existing.setFirstLogin(true);
        }

        if (form.getFullName() != null)      existing.setFullName(form.getFullName());
        if (form.getPhoneNumber() != null)   existing.setPhoneNumber(form.getPhoneNumber());
        if (form.getAddress() != null)       existing.setAddress(form.getAddress());
        if (form.getDriverLicense() != null) existing.setDriverLicense(form.getDriverLicense());
        if (form.getEmployeeId() != null)    existing.setEmployeeId(form.getEmployeeId());
        if (form.getDepartment() != null)    existing.setDepartment(form.getDepartment());

        existing.setEnabled(form.isEnabled());

        if (roles != null) {
            Set<User.Role> newRoles = new HashSet<>();
            Collections.addAll(newRoles, roles);
            existing.setRoles(newRoles);
        }

        userRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long id) {
        userRepository.deleteById(id);
    }

    // ---------- Lookups ----------
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    // ---------- Password & persistence ----------
    @Override
    @Transactional
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ---------- Lists ----------
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    // ---------- Bootstrap admin ----------
    @Override
    @Transactional
    public void initializeAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@vehiclerental.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setEnabled(true);
            admin.setFirstLogin(false);
            admin.setRoles(Collections.singleton(User.Role.ROLE_ADMIN));
            userRepository.save(admin);
        }
    }

    // ---------- Self-service profile update ----------
    @Override
    @Transactional
    public void updateOwn(String username, User form) {
        User existing = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (form.getFullName() != null)      existing.setFullName(form.getFullName());
        if (form.getPhoneNumber() != null)   existing.setPhoneNumber(form.getPhoneNumber());
        if (form.getAddress() != null)       existing.setAddress(form.getAddress());
        if (form.getDriverLicense() != null) existing.setDriverLicense(form.getDriverLicense());

        if (form.getEmail() != null && !form.getEmail().equalsIgnoreCase(existing.getEmail())) {
            if (emailExists(form.getEmail())) {
                throw new DataIntegrityViolationException("Email already exists");
            }
            existing.setEmail(form.getEmail().trim().toLowerCase());
        }

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(form.getPassword()));
            existing.setFirstLogin(false);
        }

        userRepository.save(existing);
    }

    // ---------- Self-service delete ----------
    @Override
    @Transactional
    public void deleteOwn(String username) {
        // Protect the seeded/built-in admin (optional safeguard)
        if ("admin".equalsIgnoreCase(username)) {
            throw new IllegalStateException("The built-in admin account cannot be deleted.");
        }

        // Ensure the user exists first
        userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Use the repository convenience method
        userRepository.deleteByUsername(username);
    }
}
