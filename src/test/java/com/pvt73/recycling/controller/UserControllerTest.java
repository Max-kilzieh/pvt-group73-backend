package com.pvt73.recycling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvt73.recycling.model.dao.User;
import com.pvt73.recycling.model.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.pvt73.recycling.controller.ResponseBodyMatchers.responseBody;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private final User newUser = new User("test@test.com", "Test user", "some user");

    @Autowired
    private MockMvc mvc;


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;


    @Test
    void creatReturn201() throws Exception {

        given(service.creat(newUser))
                .willReturn(newUser);

        mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(newUser, User.class));
    }

    @Test
    void findByIdReturn200() throws Exception {
        given(service.findByID(newUser.getId()))
                .willReturn(newUser);

        mvc.perform(get("/users/" + newUser.getId())
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(newUser, User.class));

    }

    @Test
    void updateReturn200() throws Exception {
        given(service.update(newUser, newUser.getId()))
                .willReturn(newUser);

        mvc.perform(put("/users/" + newUser.getId())
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(newUser, User.class));

    }

    @Test
    void deleteReturnNoContent() throws Exception {

        mvc.perform(delete("/users/" + newUser.getId())
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}