package com.pvt73.recycling;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.maps.GeoApiContext;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "KeepItClean API",
                description = "Group 73",
                version = "0.8",
                contact = @Contact(
                        name = "Max Kilzieh",
                        url = "https://github.com/Max-kilzieh",
                        email = "mkilzieh@gmail.com"
                )
        ))

@Configuration
public class Config {

    @Bean
    public Cloudinary cloudinaryApi() {
        return new Cloudinary(ObjectUtils.asMap("cloud_name", "pvt73",
                "api_key", "528765818657637",
                "api_secret", "lYOtY0RsH-OdnG4z1kBVcjyfmjs"));
    }

    @Bean
    public GeoApiContext googleGeocodingApi() {
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyB7qOZSBJKVakHWVwknSHVt8H9dY6ogUA4")
                .build();

    }
}
