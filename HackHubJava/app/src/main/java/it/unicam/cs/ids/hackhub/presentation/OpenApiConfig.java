package it.unicam.cs.ids.hackhub.presentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Metadata used to describe the HTTP API in Swagger UI. */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hackHubOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HackHub API")
                        .version("v1"));
    }

    /*
      Swagger UI: http://localhost:8080/swagger-ui/index.html
      Specifica OpenAPI JSON: http://localhost:8080/v3/api-docs
     */
}
