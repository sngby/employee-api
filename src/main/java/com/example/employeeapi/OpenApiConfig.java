package com.example.employeeapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Employee API")
                                .version("1.0")
                                .description("REST API for managing employees")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }

    @Bean
    public OpenApiCustomizer securityOpenApiCustomizer() {

        return openApi -> {
            openApi.getPaths().forEach((path, pathItem) -> {
                pathItem.readOperations().forEach(
                        operation -> {
                            if (path.equals("/employees")
                                    || path.equals("/employees/{id}")
                                    || path.equals("/admin")) {
                                operation.addSecurityItem(
                                        new SecurityRequirement()
                                                .addList("bearerAuth")
                                );
                            }
                        });
            });
        };
    }
}
