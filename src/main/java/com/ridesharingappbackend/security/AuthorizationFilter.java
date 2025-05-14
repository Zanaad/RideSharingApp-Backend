package com.ridesharingappbackend.security;

import com.ridesharingappbackend.dto.PermissionCheckRequest;
import com.ridesharingappbackend.service.AccessControlService;
import com.ridesharingappbackend.utils.ErrorResponseWriter;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
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
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔓 Allow public endpoints through
        if (path.startsWith("/api/auth") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.equals("/swagger-ui.html") ||
                path.equals("/api/companies")) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = (Claims) request.getAttribute("claims");

        if (claims == null) {
            ErrorResponseWriter.write(response, HttpStatus.UNAUTHORIZED, "Invalid token");
            return;
        }

        Integer roleId = claims.get("roleId", Integer.class);
        String method = request.getMethod();

        PermissionCheckRequest permissionRequest = new PermissionCheckRequest();
        permissionRequest.setRoleId(roleId);
        permissionRequest.setVerb(method);
        permissionRequest.setPath(path);

        if (!accessControlService.isAccessAllowed(permissionRequest)) {
            System.out.println("Access denied for roleId=" + roleId + ", path=" + path + ", method=" + method);
            ErrorResponseWriter.write(response, HttpStatus.FORBIDDEN, "Access denied");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
