// com.vehiclerental.repositories.UserRepository
package com.vehiclerental.repositories;

import com.vehiclerental.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Find by role (assuming roles is a collection on User)
    @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
    List<User> findByRole(@Param("role") User.Role role);

    // KPI helpers
    @Query("SELECT COUNT(u) FROM User u")
    Long countTotalUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    Long countActiveUsers();

    // Recent users for dashboard (uses createdAt; if you don't have it, change to OrderByIdDesc)
    List<User> findTop6ByOrderByCreatedAtDesc();

    // Search (paginated)
    @Query("""
           SELECT u FROM User u
           WHERE (:q IS NULL OR :q = ''
             OR LOWER(u.fullName)  LIKE LOWER(CONCAT('%', :q, '%'))
             OR LOWER(u.email)     LIKE LOWER(CONCAT('%', :q, '%'))
             OR LOWER(u.username)  LIKE LOWER(CONCAT('%', :q, '%')) )
           """)
    Page<User> search(@Param("q") String q, Pageable pageable);

    void deleteByUsername(String username);

    // Eager roles for security/session
    @Query("""
           SELECT u FROM User u
           LEFT JOIN FETCH u.roles
           WHERE u.username = :username
           """)
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
}
