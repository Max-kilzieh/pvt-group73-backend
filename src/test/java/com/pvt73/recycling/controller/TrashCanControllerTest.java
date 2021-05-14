package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.service.TrashCanService;
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

@WebMvcTest(TrashCanController.class)
class TrashCanControllerTest {
    private final TrashCan one = new TrashCan(new LatLng(59.40318507, 17.94220251));
    private final TrashCan two = new TrashCan(new LatLng(59.40321616, 17.94232856));
    private final TrashCan three = new TrashCan(new LatLng(59.40319188, 17.94250775));

    //Trash cans within 20 meter from Kista train station.
    private final List<TrashCan> trashCanList = Arrays.asList(one, two, three);


    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrashCanService service;

    @Test
    public void getTrashCansWithinDistance() throws Exception {


        given(service.getNearby(new LatLng(59.40332696500667, 17.942350268367566), 0, 1, 20))
                .willReturn(trashCanList);

        mvc.perform(get("/trash-cans?lat=59.40332696500667&lng=17.942350268367566&page=0&size=1&distance=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].coordinates.latitude", is(59.40318507)))
                .andExpect(jsonPath("$[0].coordinates.longitude", is(17.94220251)))
                .andExpect(jsonPath("$[1].coordinates.latitude", is(59.40321616)))
                .andExpect(jsonPath("$[1].coordinates.longitude", is(17.94232856)))
                .andExpect(jsonPath("$[2].coordinates.latitude", is(59.40319188)))
                .andExpect(jsonPath("$[2].coordinates.longitude", is(17.94250775)));
    }

    @Test
    public void getTrashCansWithPagedAndSorted() throws Exception {
        given(service.getNearby(new LatLng(59.40332696500667, 17.942350268367566), 0, 3, 0))
                .willReturn(trashCanList);

        mvc.perform(get("/trash-cans?lat=59.40332696500667&lng=17.942350268367566&page=0&size=3&distance=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].coordinates.latitude", is(59.40318507)))
                .andExpect(jsonPath("$[0].coordinates.longitude", is(17.94220251)))
                .andExpect(jsonPath("$[1].coordinates.latitude", is(59.40321616)))
                .andExpect(jsonPath("$[1].coordinates.longitude", is(17.94232856)))
                .andExpect(jsonPath("$[2].coordinates.latitude", is(59.40319188)))
                .andExpect(jsonPath("$[2].coordinates.longitude", is(17.94250775)));
    }

    @Test
    public void noContent() throws Exception {
        mvc.perform(get("/trash-cans?lat=59.40332696500667&lng=17.942350268367566&distance=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


    }

}