package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.WasteBin;
import com.pvt73.recycling.model.WasteBinService;
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

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WasteBinService service;

    @Test
    public void getNearestWasteBinsWithinDistance() throws Exception {
        WasteBin oneWithinTwentyMeterFRomKistaTrainStation = new WasteBin(59.30321616, 17.94232856);
        WasteBin twoWithinTwentyMeterFRomKistaTrainStation = new WasteBin(59.30321616, 17.94232856);
        WasteBin threeWithinTwentyMeterFRomKistaTrainStation = new WasteBin(59.30321616, 17.94232856);

        List<WasteBin> wasteBinList = Arrays.asList(oneWithinTwentyMeterFRomKistaTrainStation,
                twoWithinTwentyMeterFRomKistaTrainStation,
                threeWithinTwentyMeterFRomKistaTrainStation);

        given(service.getNearestWasteBinsWithinDistance(59.40332696500667, 17.942350268367566, 20))
                .willReturn(wasteBinList);

        mvc.perform(get("/waste_bins/nearest_waste_bins_within_distance?latitude=59.40332696500667&longitude=17.942350268367566&distance=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].latitude", is(oneWithinTwentyMeterFRomKistaTrainStation.getLatitude())))
                .andExpect(jsonPath("$[0].longitude", is(oneWithinTwentyMeterFRomKistaTrainStation.getLongitude())))
                .andExpect(jsonPath("$[1].latitude", is(twoWithinTwentyMeterFRomKistaTrainStation.getLatitude())))
                .andExpect(jsonPath("$[1].longitude", is(twoWithinTwentyMeterFRomKistaTrainStation.getLongitude())))
                .andExpect(jsonPath("$[2].latitude", is(threeWithinTwentyMeterFRomKistaTrainStation.getLatitude())))
                .andExpect(jsonPath("$[2].longitude", is(threeWithinTwentyMeterFRomKistaTrainStation.getLongitude())));
    }

    @Test
    void getNearestWasteBins() {
    }


}