package com.audition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SuppressWarnings({"PMD.TestClassWithoutTestCases", "PMD.SignatureDeclareThrowsException"})
@Configuration
 public class TestSecurityConfig {
 @Bean
 SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
 http.csrf().disable() // Disable CSRF for testing
 .authorizeRequests().anyRequest().permitAll(); // Allow all requests
 return http.build(); 
 }
}
