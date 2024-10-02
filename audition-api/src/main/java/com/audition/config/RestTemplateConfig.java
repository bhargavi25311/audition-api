package com.audition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
     RestTemplate restTemplate() {
        // Configure timeouts and other settings here
        final ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        
        // Optionally set timeouts
        ((HttpComponentsClientHttpRequestFactory) factory).setConnectTimeout(5000); // connection timeout
        ((HttpComponentsClientHttpRequestFactory) factory).setConnectionRequestTimeout(5000); // connectiontimeout
        
        return new RestTemplate(factory);
    }
}

