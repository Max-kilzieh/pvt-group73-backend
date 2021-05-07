package com.pvt73.recycling.model.service.imageService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageServiceConfig {

    @Bean
    public Cloudinary cloudinaryApi() {
        return new Cloudinary(ObjectUtils.asMap("cloud_name", "pvt73",
                "api_key", "528765818657637",
                "api_secret", "lYOtY0RsH-OdnG4z1kBVcjyfmjs"));
    }
}
