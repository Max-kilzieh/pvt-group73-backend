package com.pvt73.recycling;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KeepItClean API")
                        .version("0.1")
                        .description("Group 73")
                        .contact(new Contact().name("Max Kilzieh").email("mkilzieh@gmail.com")));


    }
}
