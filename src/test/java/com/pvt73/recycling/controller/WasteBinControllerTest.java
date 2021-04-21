package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.WasteBin;
import com.pvt73.recycling.model.service.WasteBinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WasteBinController.class)
class WasteBinControllerTest {
    private final WasteBin one = new WasteBin(59.40318507, 17.94220251);
    private final WasteBin two = new WasteBin(59.40321616, 17.94232856);
    private final WasteBin three = new WasteBin(59.40319188, 17.94250775);

    //waste bins within 20 meter from Kista train station.
    private final List<WasteBin> wasteBinList = Arrays.asList(one, two, three);


    @Autowired
    private MockMvc mvc;

    @MockBean
    private WasteBinService service;

    @Test
    public void getNearestWasteBinsWithinDistance() throws Exception {

        given(service.getNearestWasteBinsWithinDistance(59.40332696500667, 17.942350268367566, 20))
                .willReturn(wasteBinList);

        mvc.perform(get("/waste_bins/nearest_waste_bins?latitude=59.40332696500667&longitude=17.942350268367566&distance=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].latitude", is(59.40318507)))
                .andExpect(jsonPath("$[0].longitude", is(17.94220251)))
                .andExpect(jsonPath("$[1].latitude", is(59.40321616)))
                .andExpect(jsonPath("$[1].longitude", is(17.94232856)))
                .andExpect(jsonPath("$[2].latitude", is(59.40319188)))
                .andExpect(jsonPath("$[2].longitude", is(17.94250775)));
    }



    @Test
    void getNearestWasteBinsWithinHundredMeterDistance() throws Exception {

        given(service.getNearestWasteBinsWithinDistance(59.40332696500667, 17.942350268367566,100))
                .willReturn(wasteBinList);


        mvc.perform(get("/waste_bins/nearest_waste_bins?latitude=59.40332696500667&longitude=17.942350268367566")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].latitude", is(59.40318507)))
                .andExpect(jsonPath("$[0].longitude", is(17.94220251)))
                .andExpect(jsonPath("$[1].latitude", is(59.40321616)))
                .andExpect(jsonPath("$[1].longitude", is(17.94232856)))
                .andExpect(jsonPath("$[2].latitude", is(59.40319188)))
                .andExpect(jsonPath("$[2].longitude", is(17.94250775)));

    }


}