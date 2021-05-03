package com.pvt73.recycling;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Cloudinary cloudinaryApi() {
        return new Cloudinary(ObjectUtils.asMap("cloud_name", "pvt73",
                "api_key", "528765818657637",
                "api_secret", "lYOtY0RsH-OdnG4z1kBVcjyfmjs"));
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Keep it clean API")
                        .version("0.1")
                        .description("Group 73")
                        .contact(new Contact().name("Max Kilzieh").email("mkilzieh@gmail.com")));


    }
}
