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

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                System.out.println("🔐 JWT Token received: " + token);

                Claims claims = jwtService.extractClaims(token);
                System.out.println("✅ Claims extracted: " + claims);

                String email = claims.getSubject();
                Integer roleId = claims.get("roleId", Integer.class);

                if (roleId == null || email == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing roleId or email in token");
                    return;
                }

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleId);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                request.setAttribute("claims", claims);
                System.out.println("✅ Authentication set for user: " + email + " with roleId: " + roleId);

            } catch (Exception ex) {
                ex.printStackTrace();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        } else {
            // Don't block requests here — SecurityFilterChain will handle it
            System.out.println("ℹ️ No Authorization header or not a Bearer token");
        }

        filterChain.doFilter(request, response);
    }
}
