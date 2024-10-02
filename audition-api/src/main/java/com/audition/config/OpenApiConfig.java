package com.audition.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
     OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Your API Title")
                .version("1.0")
                .description("API documentation for Your Project"));
    }

    @Bean
     GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/**")
            .build();
    }
}
