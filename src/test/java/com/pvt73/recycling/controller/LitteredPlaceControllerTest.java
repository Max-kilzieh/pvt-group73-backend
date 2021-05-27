package com.pvt73.recycling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.LitteredPlace;
import com.pvt73.recycling.model.service.littered_place.LitteredPlaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LitteredPlaceController.class)
class LitteredPlaceControllerTest {
    private final LatLng KistaStation = new LatLng(59.40332696500667, 17.942350268367566);

    private final LitteredPlace newLitteredPlace = new LitteredPlace(KistaStation, "test@pvt.com");


    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LitteredPlaceService service;


    @Test
    void creat() throws Exception {
//        LitteredPlace litteredPlace = new LitteredPlace(KistaStation, "test@pvt.com");
//        litteredPlace.setCleaningStatus(CleaningStatus.NOT_CLEAN);
//
//        given(service.creat(newLitteredPlace))
//                .willReturn(litteredPlace);
//
//        mvc.perform(MockMvcRequestBuilders.post("/littered-places")
//                .content(objectMapper.writeValueAsString(litteredPlace))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
////                .andExpect(responseBody().containsObjectAsJson(litteredPlace,LitteredPlace.class));
////                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("userId", is(litteredPlace.getUserId())))
//                .andExpect(jsonPath("cleaningStatus", is("NOT_CLEAN")));


    }

    @Test
    void findById() {
    }

    @Test
    void findAllNearbyCleaningStatus() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void addImage() {

    }

    @Test
    void deleteImage() {
    }

    @Test
    void findImageById() {
    }

    @Test
    void findAllImage() {
    }


}