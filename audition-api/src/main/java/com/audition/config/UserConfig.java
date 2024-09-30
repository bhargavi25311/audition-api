package com.audition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    @Bean
     UserDetailsService users() {
        // Define users with roles
        final UserDetails user = User.builder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        final UserDetails admin = User.builder()
                .username("admin")
                .password("password")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
    
     @Bean
     
     PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    }

