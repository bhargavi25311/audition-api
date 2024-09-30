package com.audition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
 @Bean
 OpenAPI customOpenAPI() {
 return new OpenAPI()
 .info(new Info().title("Audition API").version("1.0.0")
 .description("API documentation for the Audition application.")
 .termsOfService("http://audition.com/terms/")
 .license(new License().name("Apache 2.0").url("http://audition.com/license")))
 .externalDocs(new ExternalDocumentation().description("Audition Wiki Documentation")
 .url("http://audition.com/docs"))
 .components(new Components().addSecuritySchemes("basicScheme",
 new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
 .addSecurityItem(new SecurityRequirement().addList("basicScheme"));
}
}
