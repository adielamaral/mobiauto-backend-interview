package src.com.mobiautorevenda.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String TITLE = "Mobiauto Revenda - API";
    private static final String DESCRIPTION = "Gestão de Revendas de veículos";
    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI openAPI(@Value("${VERSION.api.latest}") final String version) {
        return new OpenAPI().info(new Info().version(version).title(TITLE).description(DESCRIPTION))
                .addSecurityItem(new SecurityRequirement().addList("spring_oauth"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));

    }

}
