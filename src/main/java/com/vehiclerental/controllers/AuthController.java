package com.vehiclerental.controllers;

import com.vehiclerental.models.User;
import com.vehiclerental.services.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        // adjust view names if your templates are elsewhere
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("form") RegisterForm form,
                             BindingResult result,
                             Model model) {

        if (!form.isValidUsername()) {
            result.rejectValue("username", "invalid",
                    "Username must be 3–30 chars (letters, numbers, dot, underscore).");
        }
        if (!form.isValidPassword()) {
            result.rejectValue("password", "invalid",
                    "Password must be ≥8 with upper/lower/digit/symbol.");
        }
        if (!form.passwordsMatch()) {
            result.rejectValue("confirmPassword", "mismatch", "Passwords do not match.");
        }
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            User u = new User();
            u.setUsername(form.getUsername());
            u.setEmail(form.getEmail());
            u.setPassword(form.getPassword());
            u.setFullName(form.getUsername());
            userService.registerCustomer(u);
            return "redirect:/login?registered";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/register";
        }
    }

    // If you keep defaultSuccessUrl("/dashboard") in SecurityConfig,
    // this redirects based on granted authorities.
    @GetMapping("/dashboard")
    public String dashboardByRole(Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            switch (role) {
                case "ROLE_ADMIN":            return "redirect:/admin/dashboard";
                case "ROLE_FINANCE_MANAGER":  return "redirect:/finance/dashboard";
                case "ROLE_DRIVER":           return "redirect:/driver/dashboard";
                case "ROLE_STAFF":            return "redirect:/staff/dashboard";
                case "ROLE_CUSTOMER":         return "redirect:/customer/dashboard";
                default:                      break;
            }
        }
        return "redirect:/access-denied";
    }

    @GetMapping("/access-denied")
    public String accessDenied() { return "error/access-denied"; }

    // --- DTO ---
    public static class RegisterForm {
        @NotBlank
        @Size(min = 3, max = 30)
        @Pattern(regexp = "^[A-Za-z0-9._]+$")
        private String username;

        @NotBlank @Email
        private String email;

        @NotBlank
        @Size(min = 8)
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")
        private String password;

        @NotBlank
        private String confirmPassword;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

        public boolean isValidUsername() { return username != null && username.matches("^[A-Za-z0-9._]{3,30}$"); }
        public boolean isValidPassword() { return password != null && password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$"); }
        public boolean passwordsMatch() { return password != null && password.equals(confirmPassword); }
    }
}
