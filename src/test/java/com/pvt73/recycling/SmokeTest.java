package com.pvt73.recycling;

import com.pvt73.recycling.controller.WasteBinController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmokeTest {

    @Autowired
    private WasteBinController wasteBinController;

    @Test
    public void contextLoads() {
        assertThat(wasteBinController).isNotNull();
    }
}