// com.vehiclerental.services.UserService
package com.vehiclerental.services;

import com.vehiclerental.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Long getTotalUsers();
    Long getActiveUsers();

    boolean usernameExists(String username);
    boolean emailExists(String email);

    void registerCustomer(User user);
    void createStaffUser(User user, User.Role role);

    Page<User> search(String query, Pageable pageable);

    void createByAdmin(User user, User.Role[] roles);
    void updateByAdmin(Long id, User form, User.Role[] roles);
    void deleteByAdmin(Long id);

    User findByUsername(String username);
    User findById(Long id);

    void changePassword(User user, String newPassword);
    void save(User user);
    void deleteUser(Long id);

    List<User> getAllUsers();
    List<User> getUsersByRole(User.Role role);

    void initializeAdminUser();

    void updateOwn(String username, User form);

    // NEW: used by ProfileController
    void deleteOwn(String username);
}
