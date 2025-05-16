package com.ridesharingappbackend.config;

import com.ridesharingappbackend.security.AuthorizationFilter;
import com.ridesharingappbackend.security.JwtFilter;
import com.ridesharingappbackend.utils.ErrorResponseWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private AuthorizationFilter authorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/api/companies",
                                "/api/companies/domain/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll() // Allow these endpoints without auth
                        .anyRequest().authenticated() // Require auth for everything else
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) ->
                                ErrorResponseWriter.write(res, HttpStatus.UNAUTHORIZED, "Authentication required.")
                        )
                        .accessDeniedHandler((req, res, ex) ->
                                ErrorResponseWriter.write(res, HttpStatus.FORBIDDEN, "You do not have permission to access this resource.")
                        )
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT validation filter before auth
                .addFilterAfter(authorizationFilter, JwtFilter.class); // Add permission check after JWT validation

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}