package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.RecycleStation;
import com.pvt73.recycling.model.dao.TrashCan;
import com.pvt73.recycling.model.service.garbage_disposal.GarbageDisposalService;
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

@WebMvcTest(GarbageDisposalController.class)
class GarbageDisposalControllerTest {
    private final LatLng KistaStation = new LatLng(59.40332696500667, 17.942350268367566);

    //Trash cans nearby Kista train station.
    private final TrashCan tc0 = new TrashCan(new LatLng(59.40318507, 17.94220251));
    private final TrashCan tc1 = new TrashCan(new LatLng(59.40321616, 17.94232856));
    private final TrashCan tc2 = new TrashCan(new LatLng(59.40319188, 17.94250775));
    private final List<TrashCan> trashCanList = Arrays.asList(tc0, tc1, tc2);

    //recycle stations nearby Kista train station.
    private final RecycleStation rc0 = new RecycleStation(new LatLng(59.401594, 17.943937));
    private final RecycleStation rc1 = new RecycleStation(new LatLng(59.402342, 17.933102));
    private final RecycleStation rc2 = new RecycleStation(new LatLng(59.41293, 17.923355));
    private final List<RecycleStation> recycleStationList = Arrays.asList(rc0, rc1, rc2);


    @Autowired
    private MockMvc mvc;

    @MockBean
    private GarbageDisposalService service;

    @Test
    void getThreeTrashCansNearKistaStation() throws Exception {
        given(service.getTrashCansNearby(new LatLng(KistaStation.getLatitude(), KistaStation.getLongitude()), 0, 3))
                .willReturn(trashCanList);

        mvc.perform(get("/garbage-disposals/trash-cans")
                .param("latlng", KistaStation.toString())
                .param("offset", "0")
                .param("limit", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].coordinates.latitude", is(tc0.getCoordinates().getLatitude())))
                .andExpect(jsonPath("$[0].coordinates.longitude", is(tc0.getCoordinates().getLongitude())))
                .andExpect(jsonPath("$[1].coordinates.latitude", is(tc1.getCoordinates().getLatitude())))
                .andExpect(jsonPath("$[1].coordinates.longitude", is(tc1.getCoordinates().getLongitude())))
                .andExpect(jsonPath("$[2].coordinates.latitude", is(tc2.getCoordinates().getLatitude())))
                .andExpect(jsonPath("$[2].coordinates.longitude", is(tc2.getCoordinates().getLongitude())));
    }

    @Test
    void requestMoreTrashCansThanAvailableReturnNoContent() throws Exception {
        int allTrashCans = 12537;

        given(service.getTrashCansNearby(new LatLng(KistaStation.getLatitude(), KistaStation.getLongitude()), 1, allTrashCans))
                .willReturn(List.of());

        mvc.perform(get("/garbage-disposals/trash-cans")
                .param("latlng", KistaStation.toString())
                .param("offset", "1")
                .param("limit", String.valueOf(allTrashCans))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


    }

    @Test
    void getThreeRecycleStationNearKistaStation() throws Exception {


        given(service.getRecycleStationsNearby(new LatLng(KistaStation.getLatitude(), KistaStation.getLongitude()), 0, 3))
                .willReturn(recycleStationList);

        mvc.perform(get("/garbage-disposals/recycle-stations")
                .param("latlng", KistaStation.toString())
                .param("offset", "0")
                .param("limit", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].coordinates.latitude", is(rc0.getCoordinates().getLatitude())))
                .andExpect(jsonPath("$[0].coordinates.longitude", is(rc0.getCoordinates().getLongitude())))
                .andExpect(jsonPath("$[1].coordinates.latitude", is(rc1.getCoordinates().getLatitude())))
                .andExpect(jsonPath("$[1].coordinates.longitude", is(rc1.getCoordinates().getLongitude())))
                .andExpect(jsonPath("$[2].coordinates.latitude", is(rc2.getCoordinates().getLatitude())))
                .andExpect(jsonPath("$[2].coordinates.longitude", is(rc2.getCoordinates().getLongitude())));
    }

    @Test
    void requestMoreRecycleStationsThanAvailableReturnNoContent() throws Exception {
        int allRecycleStations = 245;

        given(service.getTrashCansNearby(new LatLng(KistaStation.getLatitude(), KistaStation.getLongitude()), 1, allRecycleStations))
                .willReturn(List.of());

        mvc.perform(get("/garbage-disposals/recycle-stations")
                .param("latlng", KistaStation.toString())
                .param("offset", "1")
                .param("limit", String.valueOf(allRecycleStations))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


    }


}