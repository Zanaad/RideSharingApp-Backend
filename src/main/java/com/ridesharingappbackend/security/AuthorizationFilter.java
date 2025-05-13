package com.ridesharingappbackend.security;

import com.ridesharingappbackend.dto.PermissionCheckRequest;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.service.AccessControlService;
import com.ridesharingappbackend.utils.ErrorResponseWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final AccessControlService accessControlService;

    public AuthorizationFilter(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user) || user.getRole() == null) {
            filterChain.doFilter(request, response);
            return;
        }

        PermissionCheckRequest accessCheck = new PermissionCheckRequest(
                user.getRole().getId(),
                request.getMethod(),
                request.getRequestURI()
        );

        boolean allowed = accessControlService.isAccessAllowed(accessCheck);

        if (!allowed) {
            ErrorResponseWriter.write(response, HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
