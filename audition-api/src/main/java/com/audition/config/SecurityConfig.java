package com.audition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@SuppressWarnings("all")
public class SecurityConfig {

    @Bean
     SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
            // Disable CSRF for simplicity; consider enabling it in production
            .csrf(csrf -> csrf.disable())
            
            // Authorize requests
            .authorizeHttpRequests(authz -> authz
                // Permit all requests to /actuator/health and /actuator/info
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                
                // Secure other actuator endpoints to ADMIN role
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                
                // Any other requests require authentication
                .anyRequest().authenticated()
            )
            
            // HTTP Basic Authentication
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
}
