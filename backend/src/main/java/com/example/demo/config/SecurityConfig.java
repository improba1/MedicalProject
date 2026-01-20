package com.example.demo.config;

import com.example.demo.security.CustomAccessDeniedHandler;
import com.example.demo.security.CustomAuthenticationEntryPoint;
import com.example.demo.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.demo.enums.Permission.*;
import static com.example.demo.enums.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final ApiProperties api;
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAuthenticationEntryPoint  customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler  customAccessDeniedHandler;


    private String[] whiteList() {
        return new String[]{
                api.getPrefix() + "/auth/**",
                api.getPrefix() + "/doctors/public/**",
                api.getPrefix() + "/images/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(req ->
                        req.requestMatchers(whiteList()).permitAll()
                                .requestMatchers(POST, api.getPrefix() + "/auth/logout").hasAuthority("user:logout")
                                .requestMatchers(api.getPrefix() + "/admin/**").hasRole(ADMIN.name())
                                .requestMatchers(GET, api.getPrefix() + "/admin/**").hasAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, api.getPrefix() + "/admin/**").hasAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, api.getPrefix() + "/admin/**").hasAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, api.getPrefix() + "/admin/**").hasAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(api.getPrefix() + "/superadmin/**").hasRole(SUPERADMIN.name())
                                .requestMatchers(GET, api.getPrefix() + "/superadmin/**").hasAuthority(SUPERADMIN_CREATE.getPermission())
                                .requestMatchers(DELETE, api.getPrefix() + "/superadmin/**").hasAuthority(SUPERADMIN_DELETE.getPermission())
                                .requestMatchers(api.getPrefix() + "/doctor/**").hasRole(DOCTOR.name())
                                .requestMatchers(GET, api.getPrefix() + "/doctor/**").hasAnyAuthority(DOCTOR_READ.getPermission(), ADMIN_READ.getPermission())
                                .requestMatchers(POST, api.getPrefix() + "/doctor/**").hasAnyAuthority(DOCTOR_CREATE.getPermission(), ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, api.getPrefix() + "/doctor/**").hasAnyAuthority(DOCTOR_UPDATE.getPermission(), ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, api.getPrefix() + "/doctor/**").hasAnyAuthority(DOCTOR_DELETE.getPermission(), ADMIN_DELETE.getPermission())
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}