package com.pvt73.recycling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvt73.recycling.model.dao.Event;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.service.event.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.pvt73.recycling.controller.ResponseBodyMatchers.responseBody;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    private final Event event = new Event("Test user");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService service;


    @Test
    void creat() throws Exception {
        given(service.creat(event))
                .willReturn(event);

        mvc.perform(post("/events")
                .content(objectMapper.writeValueAsString(event))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(event, Event.class));

    }

    @Test
    void update() throws Exception {
        given(service.update(event, 0)).willReturn(event);

        mvc.perform(put("/events/0")
                .content(objectMapper.writeValueAsString(event))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(event, Event.class));
    }

    @Test
    void deleteEventsReturnNoContent() throws Exception {

        mvc.perform(delete("/events/0"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findById() throws Exception {
        event.setCoordinates(new LatLng(57.1, 17.2));

        given(service.findById(0, event.getCoordinates()))
                .willReturn(event);

        mvc.perform(get("/events/0")
                .param("latlng", event.getCoordinates().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(event, Event.class));

    }

    @Test
    void findAllNearbyReturn200() throws Exception {
        event.setCoordinates(new LatLng(57.1, 17.2));
        Event otherEvent = new Event("Test user");
        otherEvent.setCoordinates(new LatLng(57.5, 17.4));
        List<Event> eventList = Arrays.asList(event, otherEvent);

        given(service.findAllNearby(event.getCoordinates(), 0, 2))
                .willReturn(eventList);

        mvc.perform(get("/events")
                .param("latlng", event.getCoordinates().toString())
                .param("offset", "0")
                .param("limit", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void findAllNearbyReturnNoContent() throws Exception {
        event.setCoordinates(new LatLng(57.1, 17.2));


        given(service.findAllNearby(event.getCoordinates(), 0, 2))
                .willReturn(List.of());

        mvc.perform(get("/events")
                .param("latlng", event.getCoordinates().toString())
                .param("offset", "0")
                .param("limit", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }


    @Test
    void allEventsUserParticipatedReturn200() throws Exception {

        event.setCoordinates(new LatLng(57.1, 17.2));
        Event otherEvent = new Event("Test user");
        otherEvent.setCoordinates(new LatLng(57.5, 17.4));
        List<Event> eventList = Arrays.asList(event, otherEvent);

        given(service.findAllParticipatedByUser("Test user"))
                .willReturn(eventList);

        mvc.perform(get("/events/participants/" + event.getUserId())

                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));


    }

    @Test
    void allEventsUserParticipatedReturnNoContent() throws Exception {

        event.setCoordinates(new LatLng(57.1, 17.2));


        given(service.findAllParticipatedByUser("Test user"))
                .willReturn(List.of());

        mvc.perform(get("/events/participants/" + event.getUserId())

                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


    }

    @Test
    void allEventsCreatedByUserReturn200() throws Exception {
        event.setCoordinates(new LatLng(57.1, 17.2));
        Event otherEvent = new Event("Test user");
        otherEvent.setCoordinates(new LatLng(57.5, 17.4));
        List<Event> eventList = Arrays.asList(event, otherEvent);

        given(service.findAllCreatedByUser("Test user"))
                .willReturn(eventList);

        mvc.perform(get("/events/craters/" + event.getUserId())

                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));


    }

    @Test
    void allEventsCreatedByUserReturnNoContent() throws Exception {
        event.setCoordinates(new LatLng(57.1, 17.2));


        given(service.findAllCreatedByUser("Test user"))
                .willReturn(List.of());

        mvc.perform(get("/events/craters/" + event.getUserId())

                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


    }
}