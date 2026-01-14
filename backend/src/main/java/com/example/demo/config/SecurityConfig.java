package com.example.demo.config;

import com.example.demo.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.demo.enums.Permission.*;
import static com.example.demo.enums.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/doctors/search",
            "/api/v1/doctors/get-all",
            "/api/v1/images/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()

                                .requestMatchers(POST, apiPrefix + "/auth/logout").hasAuthority("user:logout")

                                // ADMIN доступ
                                .requestMatchers(apiPrefix + "/admin/**").hasRole(ADMIN.name())
                                .requestMatchers(GET, apiPrefix + "/admin/**").hasAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, apiPrefix + "/admin/**").hasAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, apiPrefix + "/admin/**").hasAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, apiPrefix + "/admin/**").hasAuthority(ADMIN_DELETE.getPermission())

                                // SUPERADMIN доступ
                                .requestMatchers(apiPrefix + "/superadmin/**").hasRole(SUPERADMIN.name())
                                .requestMatchers(GET, apiPrefix + "/superadmin/**").hasAuthority(SUPERADMIN_CREATE.getPermission())
                                .requestMatchers(DELETE, apiPrefix + "/superadmin/**").hasAuthority(SUPERADMIN_DELETE.getPermission())

                                // DOCTOR доступ
                                .requestMatchers(apiPrefix + "/doctor/**").hasRole(DOCTOR.name())
                                .requestMatchers(GET, apiPrefix + "/doctor/**").hasAnyAuthority(DOCTOR_READ.getPermission(), ADMIN_READ.getPermission())
                                .requestMatchers(POST, apiPrefix + "/doctor/**").hasAnyAuthority(DOCTOR_CREATE.getPermission(), ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, apiPrefix + "/doctor/**").hasAnyAuthority(DOCTOR_UPDATE.getPermission(), ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, apiPrefix + "/doctor/**").hasAnyAuthority(DOCTOR_DELETE.getPermission(), ADMIN_DELETE.getPermission())
                                .anyRequest().authenticated()
                )

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}