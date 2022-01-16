package com.mkjb.notes;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Super Notes",
                version = "1.0.0",
                description = "The Super Notes application allows to manage your notes",
                contact = @Contact(name = "Krzysiek", email = "krzysiek@mkjb.pl")
        ),
        servers = {
                @Server(url = "http://localhost:8080")
        },

        security = {
                @SecurityRequirement(name = "OIDC", scopes = {})
        }
)

@SecurityScheme(name = "OIDC",
        type = SecuritySchemeType.OPENIDCONNECT,
        openIdConnectUrl = "https://accounts.google.com/.well-known/openid-configuration"
)

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
