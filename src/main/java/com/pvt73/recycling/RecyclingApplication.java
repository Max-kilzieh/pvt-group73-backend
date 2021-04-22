package com.pvt73.recycling;

import com.pvt73.recycling.model.service.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class RecyclingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecyclingApplication.class);
    }

}

