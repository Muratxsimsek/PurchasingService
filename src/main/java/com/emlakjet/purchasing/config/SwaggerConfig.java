package com.emlakjet.purchasing.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Purchasing API",
                version = "v1.0",
                description = "API documentation for the purchasing system",
                contact = @Contact(
                        name = "Support Team",
                        email = "muratxsimsek@gmail.com"
                ),
                license = @License(
                        name = "Emlak Jet",
                        url = "https://www.emlakjet.com/"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Purchasing Documentation",
                url = "https://github.com/example/purchasing-docs"
        ),
        security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "You can use POST /api/auth/login endpoint in authentication-controller to get the JWT token"
)
@Configuration
public class SwaggerConfig {

}
