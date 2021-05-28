package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.Address;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.service.address.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AddressService service;

    @Test
    void getAddress() throws Exception {
        Address address = new Address(new LatLng(59.1, 17.2), "test address");
        given(service.getAddress(address.getCoordinates())).willReturn(address);

        mvc.perform(get("/addresses")
                .param("latlng", address.getCoordinates().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("coordinates.latitude", is(59.1)))
                .andExpect(jsonPath("coordinates.longitude", is(17.2)))
                .andExpect(jsonPath("adress", is(address.getAdress())));

    }
}