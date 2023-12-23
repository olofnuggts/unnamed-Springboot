package com.yk.unnamed.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Enable CSRF protection with CSRF token stored in a cookie
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                // Define authorization rules
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/public/**").permitAll() // Allow public access to certain paths
                        .anyRequest().authenticated() // Require authentication for other requests
                )

                // Configure form login (customize as needed)
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())

                // Configure logout (customize as needed)
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout"));

        return http.build();
    }
}
