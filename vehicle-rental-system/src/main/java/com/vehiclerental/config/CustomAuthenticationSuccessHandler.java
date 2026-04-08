// src/main/java/com/vehiclerental/config/CustomAuthenticationSuccessHandler.java
package com.vehiclerental.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Use Security's authorities (no DB access, no lazy init risks)
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_FINANCE_MANAGER")) {
            response.sendRedirect("/finance/dashboard");
        } else if (roles.contains("ROLE_DRIVER")) {
            response.sendRedirect("/driver/dashboard");
        } else if (roles.contains("ROLE_STAFF")) {
            response.sendRedirect("/staff/dashboard");
        } else if (roles.contains("ROLE_CUSTOMER")) {
            response.sendRedirect("/customer/dashboard"); // <- changed from /change-password
        } else {
            response.sendRedirect("/"); // sensible default
        }
    }
}
