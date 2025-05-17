package com.ridesharingappbackend.security;

import com.ridesharingappbackend.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Validate Bearer token format
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                Claims claims = jwtService.extractClaims(token);

                Integer companyId = claims.get("companyId", Integer.class);
                Integer userId = claims.get("userId", Integer.class);
                Integer roleId = claims.get("roleId", Integer.class);
                String email = claims.getSubject();

                if (roleId == null || email == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing roleId or email in token");
                    return;
                }
                request.setAttribute("companyId", companyId);
                request.setAttribute("userId", userId);
                request.setAttribute("claims", claims);

                // Set authentication context
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleId);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}