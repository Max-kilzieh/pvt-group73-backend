package com.pvt73.recycling.integrationTest;

import com.pvt73.recycling.controller.ImageController;
import com.pvt73.recycling.controller.WasteBinController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmokeTest {

    @Autowired
    private WasteBinController wasteBinController;

    @Autowired
    private ImageController imageController;

    @Test
    public void contextLoadWasteBinController() {
        assertThat(wasteBinController).isNotNull();
    }

    @Test
    public void contextLoadImageController() {
        assertThat(imageController).isNotNull();
    }
}