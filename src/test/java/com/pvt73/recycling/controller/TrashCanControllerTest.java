//package com.pvt73.recycling.controller;
//
//import com.pvt73.recycling.model.dao.LatLng;
//import com.pvt73.recycling.model.dao.TrashCan;
//import com.pvt73.recycling.model.service.trash_can.TrashCanService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(GarbageDisposalController.class)
//class TrashCanControllerTest {
//    private final TrashCan zero = new TrashCan(new LatLng(59.40318507, 17.94220251));
//    private final TrashCan one = new TrashCan(new LatLng(59.40321616, 17.94232856));
//    private final TrashCan two = new TrashCan(new LatLng(59.40319188, 17.94250775));
//
//    //Trash cans within 20 meter from Kista train station.
//    private final List<TrashCan> trashCanList = Arrays.asList(zero, one, two);
//
//    private final LatLng KistaStation = new LatLng(59.40332696500667, 17.942350268367566);
//
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private TrashCanService service;
//
//    @Test
//    public void getThreeTrashCansNearKistaStation() throws Exception {
//
//
//        given(service.getNearby(new LatLng(KistaStation.getLatitude(), KistaStation.getLongitude()), 0, 3))
//                .willReturn(trashCanList);
//
//        mvc.perform(get("/trash-cans?lat=59.40332696500667&lng=17.942350268367566&offset=0&limit=3")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].coordinates.latitude", is(zero.getCoordinates().getLatitude())))
//                .andExpect(jsonPath("$[0].coordinates.longitude", is(zero.getCoordinates().getLongitude())))
//                .andExpect(jsonPath("$[1].coordinates.latitude", is(one.getCoordinates().getLatitude())))
//                .andExpect(jsonPath("$[1].coordinates.longitude", is(one.getCoordinates().getLongitude())))
//                .andExpect(jsonPath("$[2].coordinates.latitude", is(two.getCoordinates().getLatitude())))
//                .andExpect(jsonPath("$[2].coordinates.longitude", is(two.getCoordinates().getLongitude())));
//    }
//
//
//    @Test
//    public void requestMoreTrashCansThanAvailableReturnNoContent() throws Exception {
//        int allTrashCans = 12537;
//
//        given(service.getNearby(new LatLng(KistaStation.getLatitude(), KistaStation.getLongitude()), 1, allTrashCans))
//                .willReturn(List.of());
//
//        mvc.perform(get("/trash-cans?lat=59.40332696500667&lng=17.942350268367566")
//                .param("offset", "1")
//                .param("limit", String.valueOf(allTrashCans))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//
//    }
//
//}